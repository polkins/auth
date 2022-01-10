package ru.nonsense.auth.service;

import ru.nonsense.api.dto.UserDto;
import ru.nonsense.auth.domain.entity.user.AuthUser;

import java.util.List;
import java.util.Optional;

public interface AuthUserService {
    public List<AuthUser> createUsers(List<UserDto> userDtos);
    // for testing cache key
    public Optional<AuthUser> findUser(UserDto userDto);
    public AuthUser findByLogin(String login);
}
