package ru.nonsense.auth.service;

import ru.nonsense.api.dto.UserDto;
import ru.nonsense.auth.domain.entity.user.AuthUser;

import java.util.List;
import java.util.Optional;


public interface AuthUserService {
    List<UserDto> createUsers(List<UserDto> userDtos);
    // for testing cache key
    Optional<AuthUser> findUser(UserDto userDto);
    AuthUser findByLogin(String login);
}
