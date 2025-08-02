package ru.practicum.requests;



import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByEventIdAndStatus(Long eventId, RequestStatus status);

    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findAllByIdInAndEventId(List<Long> ids, Long eventId);

    long countByEventIdAndStatus(Long eventId, RequestStatus status);
}

