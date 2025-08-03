package ru.practicum.categories.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCategoryDto {

    @NotBlank(message = "Название категории не может быть пустым")
    @Size(min = 1, max = 50, message = "Название категории должно содержать от 1 до 50 символов")
    private String name;
}
