package ru.practicum.users.model;

import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface AdminUserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);
    UserDto createUser(NewUserRequest dto);
    void deleteUser(Long id);
}