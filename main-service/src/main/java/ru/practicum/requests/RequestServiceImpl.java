package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventState;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.model.UserRepository;


import java.time.LocalDateTime;
import java.util.List;
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
}
