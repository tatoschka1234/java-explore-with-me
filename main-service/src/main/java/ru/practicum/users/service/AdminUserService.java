package ru.practicum.users.service;

import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.NewUserRequest;

import java.util.List;

public interface AdminUserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto createUser(NewUserRequest dto);

    void deleteUser(Long id);
}
