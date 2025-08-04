package ru.practicum.comments.service;

import ru.practicum.comments.dto.AdminCommentDto;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.PublicCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Long userId, Long eventId, NewCommentDto dto);

    CommentDto updateComment(Long userId, Long commentId, NewCommentDto dto);

    void deleteComment(Long userId, Long commentId);

    List<AdminCommentDto> getUserCommentsForAdmin(Long userId);

    List<PublicCommentDto> getPublicCommentsForEvent(Long eventId);

    void deleteCommentAsAdmin(Long commentId);

    List<AdminCommentDto> getAllCommentsAsAdmin();

    List<AdminCommentDto> getCommentsByEventAsAdmin(Long eventId);

}
