package ru.practicum.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentStatus;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByAuthorIdOrderByCreatedAtDesc(Long userId);

    Optional<Comment> findByIdAndAuthorId(Long commentId, Long authorId);

    List<Comment> findByEventIdAndStatusOrderByUpdatedAtDesc(Long eventId, CommentStatus status);

    List<Comment> findAllByEventIdOrderByUpdatedAtDesc(Long eventId);

    List<Comment> findAllByOrderByUpdatedAtDesc();

}
