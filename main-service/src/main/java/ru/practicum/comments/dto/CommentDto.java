package ru.practicum.comments.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private Long authorId;
    private Long eventId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
