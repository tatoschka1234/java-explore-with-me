package ru.practicum.users.model;

import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserShortDto;

public class UserMapper {

    public static User toEntity(NewUserRequest dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto toShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}