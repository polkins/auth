package ru.nonsense.auth.mapper;

import org.mapstruct.Mapper;
import ru.nonsense.api.dto.ClientDto;
import ru.nonsense.auth.domain.entity.client.Client;

@Mapper
public interface AuthClientDtoMapper extends AbstractMapper<Client, ClientDto> {
}
