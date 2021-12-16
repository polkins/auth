package ru.nonsense.auth.mapper;

import org.mapstruct.Mapper;
import ru.nonsense.api.dto.UserDto;
import ru.nonsense.auth.domain.entity.user.AuthUser;

@Mapper
public interface AuthUserDtoMapper extends AbstractMapper<AuthUser, UserDto> {
}
