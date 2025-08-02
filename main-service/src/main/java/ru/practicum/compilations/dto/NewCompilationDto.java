package ru.practicum.compilations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilationDto {
    @NotBlank(message = "не должно быть пустым")
    @Size(min = 1, max = 50, message = "title Compilation должно содержать от 1 до 50 символов")
    private String title;
    private boolean pinned;
    private Set<Long> events;
}
