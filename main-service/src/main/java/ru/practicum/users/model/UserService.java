package ru.practicum.users.model;

public interface UserService {
    User getUserById(Long userId);
    User getUser(Long userId);

}
