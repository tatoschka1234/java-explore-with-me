package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventState;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ForbiddenException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found: " + userId)
        );

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event not found: " + eventId)
        );

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Initiator cannot send request to own event.");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Event is not published.");
        }

        int limit = event.getParticipantLimit();
        if (limit != 0) {
            long confirmed = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
            if (confirmed >= limit) {
                throw new ConflictException("Participant limit reached");
            }
        }

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Request already exists.");
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .status(event.getParticipantLimit() == 0 || !event.isRequestModeration()
                        ? RequestStatus.CONFIRMED
                        : RequestStatus.PENDING)
                .event(event)
                .requester(user)
                .build();

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found: " + userId)
        );

        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Request not found: " + requestId)
        );

        if (!request.getRequester().getId().equals(userId)) {
            throw new ConflictException("User is not the owner of the request.");
        }

        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatuses(long userId, long eventId,
                                                                EventRequestStatusUpdateRequest body) {
        if (body == null || body.getRequestIds() == null || body.getRequestIds().isEmpty()) {
            throw new BadRequestException("requestIds must not be empty");
        }
        if (body.getStatus() == null) {
            throw new BadRequestException("status must not be null");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        if (event.getInitiator() == null || !Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenException("Only the event initiator can manage requests");
        }

        List<Request> requests = requestRepository.findAllByIdInAndEventId(body.getRequestIds(), eventId);
        if (requests.size() != body.getRequestIds().size()) {
            throw new NotFoundException("Some requests not found for this event");
        }

        boolean hasNonPending = requests.stream()
                .anyMatch(r -> r.getStatus() != RequestStatus.PENDING);
        if (hasNonPending) {
            throw new ConflictException("Only PENDING requests can be updated");
        }

        int limit = event.getParticipantLimit();
        long confirmed = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        long available = (limit == 0) ? Long.MAX_VALUE : Math.max(0, (long) limit - confirmed);

        List<Request> toConfirm = new ArrayList<>();
        List<Request> toReject = new ArrayList<>();

        switch (body.getStatus()) {
            case CONFIRMED -> {

                if (limit != 0) {
                    if (available == 0) {
                        throw new ConflictException("Participant limit reached");
                    }
                    if (available < requests.size()) {
                        throw new ConflictException("Participant limit reached");
                    }
                }

                for (Request r : requests) {
                    r.setStatus(RequestStatus.CONFIRMED);
                }
                toConfirm.addAll(requests);
            }
            case REJECTED -> {
                for (Request r : requests) {
                    r.setStatus(RequestStatus.REJECTED);
                }
                toReject.addAll(requests);
            }
            default -> throw new BadRequestException("Unsupported status action: " + body.getStatus());
        }

        requestRepository.saveAll(requests);

        long newConfirmed = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        event.setConfirmedRequests((int) newConfirmed);
        eventRepository.save(event);

        List<ParticipationRequestDto> confirmedDtos = toConfirm.stream()
                .map(RequestMapper::toDto)
                .toList();
        List<ParticipationRequestDto> rejectedDtos = toReject.stream()
                .map(RequestMapper::toDto)
                .toList();

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedDtos)
                .rejectedRequests(rejectedDtos)
                .build();
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsForOwnEvent(long userId, long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenException("Only the event initiator can view requests");
        }

        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return requests.stream()
                .map(RequestMapper::toDto)
                .toList();
    }
}
