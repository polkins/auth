package ru.nonsense.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.annotations.Test;
import ru.nonsense.api.dto.ClientDto;
import ru.nonsense.api.dto.UserDto;
import ru.nonsense.auth.common.AbstractIntegrationTest;
import ru.nonsense.auth.domain.entity.client.Client;
import ru.nonsense.auth.domain.entity.user.AuthUser;
import ru.nonsense.auth.invocationhandler.CacheService;
import ru.nonsense.auth.mapper.AuthClientDtoMapper;
import ru.nonsense.auth.service.AuthUserService;
import ru.nonsense.auth.utils.JWTUtil;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.nonsense.auth.ParametrizedTypeReferenceHolder.CLIENTS_REFERENCE;
import static ru.nonsense.auth.ParametrizedTypeReferenceHolder.STRING_REFERENCE;
import static ru.nonsense.auth.ParametrizedTypeReferenceHolder.USERS_REFERENCE;
import static ru.nonsense.auth.utils.AuthUtils.API_PREFIX;
import static ru.nonsense.auth.utils.AuthUtils.AUTH;
import static ru.nonsense.auth.utils.AuthUtils.FULL_PATH_CREATE_CLIENTS;
import static ru.nonsense.auth.utils.AuthUtils.FULL_PATH_CREATE_USERS;
import static ru.nonsense.auth.utils.AuthUtils.TOKEN;

public class AuthControllerTest extends AbstractIntegrationTest {
    @Autowired
    private AuthClientDtoMapper authClientDtoMapper;
    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void authorizeByJWT() {
        // given
        ClientDto clientDto = new ClientDto()
                .setFirstName("Сергей")
                .setLastName("Сергеев")
                .setSurName("Сергеевич");

        UserDto userDto = new UserDto()
                .setFirstName("Иванов")
                .setLastName("Иван")
                .setSurName("Иванович")
                .setLogin("ivanov")
                .setPassword("1");

        MultiValueMap<String, String> prams = new LinkedMultiValueMap<>();
        prams.add("username", userDto.getLogin());
        prams.add("password", userDto.getPassword());


        //when
        // создать клиента
        HttpEntity<?> request = new HttpEntity<>(List.of(clientDto), getHeaders());
        ResponseEntity<List<Client>> createClient = restTemplate.exchange(
                FULL_PATH_CREATE_CLIENTS,
                HttpMethod.POST,
                request,
                CLIENTS_REFERENCE
        );

        //создать юзера
        userDto.setClients(authClientDtoMapper.toDto(createClient.getBody()));
        HttpEntity<?> request2 = new HttpEntity<>(List.of(userDto), getHeaders());
        ResponseEntity<List<AuthUser>> createUser = restTemplate.exchange(
                FULL_PATH_CREATE_USERS,
                HttpMethod.POST,
                request2,
                USERS_REFERENCE
        );

        //получить jwt токен
        HttpEntity<?> request3 = new HttpEntity<>(prams, getHeaders());
        var jwt = restTemplate.exchange(
                API_PREFIX + AUTH + TOKEN,
                HttpMethod.POST,
                request3,
                STRING_REFERENCE
        );

        // авторизоваться на /hello где настроено httpSecurity.authorizeRequests().anyRequest().authenticated();
        // с помощью токена
        var headers = getHeaders();
        headers.add("Authorization", "Bearer " + jwt.getBody());
        HttpEntity<?> request4 = new HttpEntity<>(headers);
        var auth = restTemplate.exchange(
                API_PREFIX + AUTH + "/hello",
                HttpMethod.GET,
                request4,
                STRING_REFERENCE
        );

        //then
        var clientId = createClient.getBody().get(0).getId();
        assertThat(clientId).isNotNull();

        // проверить что в токене присутствует id клиента(-ов) и фио юзера
        String jwtBody = jwt.getBody();
        assertThat(jwtBody).isNotNull();
        var claims = jwtUtil.getClaims(jwtBody);
        var list = (List) claims.get("clients");
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0)).isEqualTo(clientId.intValue());
        assertThat(claims.get("user")).isEqualTo(userDto.getFirstName() + userDto.getSurName() + userDto.getLastName());

        // проверить что авторизация прошла успешно
        assertThat(auth.getBody()).isNotNull();
        assertThat(auth.getBody()).isEqualTo("Hello, you are authorized");
    }
}