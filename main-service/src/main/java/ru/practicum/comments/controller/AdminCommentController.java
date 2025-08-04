package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.AdminCommentDto;
import ru.practicum.comments.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService commentService;

    @GetMapping("/users/{userId}/comments")
    public List<AdminCommentDto> getAllUserComments(@PathVariable Long userId) {
        return commentService.getUserCommentsForAdmin(userId);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteCommentAsAdmin(commentId);
    }

    @GetMapping("/comments")
    public List<AdminCommentDto> getAllComments() {
        return commentService.getAllCommentsAsAdmin();
    }

    @GetMapping("/events/{eventId}/comments")
    public List<AdminCommentDto> getCommentsByEvent(@PathVariable Long eventId) {
        return commentService.getCommentsByEventAsAdmin(eventId);
    }
}
