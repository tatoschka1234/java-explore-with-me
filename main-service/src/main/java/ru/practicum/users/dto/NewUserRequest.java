package ru.practicum.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserRequest {

    @NotBlank(message = "Email не должен быть пустым")
    @Email
    @Size(min = 6, max = 254, message = "Email должен содержать от 6 до 254 символов")
    private String email;

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 250, message = "Имя должно содержать от 2 до 250 символов")
    private String name;
}
