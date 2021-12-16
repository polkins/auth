package ru.nonsense.auth;

import org.springframework.core.ParameterizedTypeReference;
import ru.nonsense.auth.domain.entity.client.Client;
import ru.nonsense.auth.domain.entity.user.AuthUser;

import java.util.List;

@SuppressWarnings("Convert2Diamond")
public class ParametrizedTypeReferenceHolder {

    public static final ParameterizedTypeReference<String> STRING_REFERENCE = new
            ParameterizedTypeReference<>() {
            };

    public static final ParameterizedTypeReference<List<AuthUser>> USERS_REFERENCE = new
            ParameterizedTypeReference<>() {
            };

    public static final ParameterizedTypeReference<List<Client>> CLIENTS_REFERENCE = new
            ParameterizedTypeReference<>() {
            };
}
