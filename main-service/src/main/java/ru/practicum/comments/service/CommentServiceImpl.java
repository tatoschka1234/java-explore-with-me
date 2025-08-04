package ru.practicum.comments.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.AdminCommentDto;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.PublicCommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentStatus;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventState;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ForbiddenException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private static final Duration EDITABLE_PERIOD = Duration.ofHours(24);

    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("Cannot comment on unpublished events.");
        }

        Comment comment = CommentMapper.toEntity(dto, user, event);
        commentRepository.save(comment);
        return CommentMapper.toDto(comment);
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto dto) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new ForbiddenException("Comment not found or access denied"));

        if (comment.getStatus() != CommentStatus.ACTIVE) {
            throw new ForbiddenException("Cannot update deleted comment.");
        }

        if (isExpired(comment.getCreatedAt())) {
            throw new ForbiddenException("Comment can only be updated within 24 hours.");
        }

        comment.setText(dto.getText());
        comment.setUpdatedAt(LocalDateTime.now());
        return CommentMapper.toDto(comment);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Comment not found or access denied"));

        if (comment.getStatus() != CommentStatus.ACTIVE) {
            throw new ForbiddenException("Comment is already deleted.");
        }

        if (isExpired(comment.getCreatedAt())) {
            throw new ForbiddenException("Comment can only be deleted within 24 hours.");
        }

        comment.setStatus(CommentStatus.DELETED_BY_USER);
        comment.setUpdatedAt(LocalDateTime.now());
    }


    @Override
    public List<AdminCommentDto> getUserCommentsForAdmin(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found.");
        }
        return commentRepository.findByAuthorIdOrderByCreatedAtDesc(userId).stream()
                .map(CommentMapper::toAdminDto)
                .toList();
    }

    @Override
    public void deleteCommentAsAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (comment.getStatus() == CommentStatus.DELETED_BY_ADMIN) return;

        comment.setStatus(CommentStatus.DELETED_BY_ADMIN);
        comment.setUpdatedAt(LocalDateTime.now());
    }

    private boolean isExpired(LocalDateTime createdAt) {
        return createdAt.plus(EDITABLE_PERIOD).isBefore(LocalDateTime.now());
    }

    @Override
    public List<PublicCommentDto> getPublicCommentsForEvent(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found."));
        List<Comment> comments = commentRepository.findByEventIdAndStatusOrderByUpdatedAtDesc(eventId, CommentStatus.ACTIVE);
        return comments.stream()
                .map(CommentMapper::toPublicDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<AdminCommentDto> getAllCommentsAsAdmin() {
        return commentRepository.findAllByOrderByUpdatedAtDesc().stream()
                .map(CommentMapper::toAdminDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminCommentDto> getCommentsByEventAsAdmin(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found."));

        return commentRepository.findAllByEventIdOrderByUpdatedAtDesc(eventId).stream()
                .map(CommentMapper::toAdminDto)
                .collect(Collectors.toList());
    }

}
