package ru.practicum.comments.dto;

import lombok.*;
import ru.practicum.comments.model.CommentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCommentDto {
    private Long id;
    private String text;
    private Long authorId;
    private Long eventId;
    private CommentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
