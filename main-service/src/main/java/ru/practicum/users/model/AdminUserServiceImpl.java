package ru.practicum.users.model;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllById(ids).stream()
                    .map(UserMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public UserDto createUser(NewUserRequest dto) {
        User user = userRepository.save(UserMapper.toEntity(dto));
        return UserMapper.toDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with id=" + id);
        }
        userRepository.deleteById(id);
    }
}
