package ru.practicum.requests;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByEventId(Long eventId);

    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findAllByIdInAndEventId(List<Long> ids, Long eventId);

    long countByEventIdAndStatus(Long eventId, RequestStatus status);
}
