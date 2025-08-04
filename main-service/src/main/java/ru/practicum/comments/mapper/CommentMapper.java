package ru.practicum.comments.mapper;

import ru.practicum.comments.dto.AdminCommentDto;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.PublicCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentStatus;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        if (comment == null) return null;

        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorId(comment.getAuthor().getId())
                .eventId(comment.getEvent().getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public static AdminCommentDto toAdminDto(Comment comment) {
        if (comment == null) return null;

        return AdminCommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorId(comment.getAuthor().getId())
                .eventId(comment.getEvent().getId())
                .status(comment.getStatus())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public static Comment toEntity(NewCommentDto dto, User author, Event event) {
        if (dto == null || author == null || event == null) return null;
        LocalDateTime time = LocalDateTime.now();
        return Comment.builder()
                .text(dto.getText())
                .author(author)
                .event(event)
                .status(CommentStatus.ACTIVE)
                .createdAt(time)
                .updatedAt(time)
                .build();
    }

    public static PublicCommentDto toPublicDto(Comment comment) {
        if (comment == null) return null;

        return PublicCommentDto.builder()
                .text(comment.getText())
                .authorId(comment.getAuthor().getId())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

}


