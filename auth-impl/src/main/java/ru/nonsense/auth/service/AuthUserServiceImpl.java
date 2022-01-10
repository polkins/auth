package ru.nonsense.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nonsense.api.dto.UserDto;
import ru.nonsense.auth.annotation.MyCacheable;
import ru.nonsense.auth.domain.entity.user.AuthUser;
import ru.nonsense.auth.mapper.AuthUserDtoMapper;
import ru.nonsense.auth.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {
    private final PasswordEncoder passwordEncoder;
    private final AuthUserDtoMapper authUserDtoMapper;
    private final UserRepository userRepository;

    @Override
    public List<AuthUser> createUsers(List<UserDto> userDtos) {
        userDtos.forEach(u -> u.setPassword(passwordEncoder.encode(u.getPassword())));
        return userRepository.saveAll(authUserDtoMapper.toDomainModel(userDtos));
    }

    @Override
    @MyCacheable(name = "users", key = "#userDto.id")
    public Optional<AuthUser> findUser(UserDto userDto) {
        return userRepository.findById(userDto.getId());
    }

    @Override
    public AuthUser findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
