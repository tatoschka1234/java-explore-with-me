package ru.practicum.requests;



import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
