package ru.nonsense.auth.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.nonsense.auth.client.api.dto.ClientDto;
import ru.nonsense.auth.client.api.dto.UserDto;

import java.util.List;

import static ru.nonsense.auth.client.util.AuthUtils.CREATE_CLIENTS;
import static ru.nonsense.auth.client.util.AuthUtils.CREATE_USERS;
import static ru.nonsense.auth.client.util.AuthUtils.LOGIN;
import static ru.nonsense.auth.client.util.AuthUtils.TOKEN;

@FeignClient(name = "auth", url = "${auth.url}")
public interface AuthControllerFeign {
    @PostMapping(value = TOKEN, consumes = "application/json", produces = "application/json")
    ResponseEntity<String> getJwtToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                       @RequestParam(value = "username") String username,
                                       @RequestParam(value = "password") String password);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = CREATE_USERS, consumes = "application/json", produces = "application/json")
    ResponseEntity<List<UserDto>> createUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody List<UserDto> userDtos);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = CREATE_CLIENTS, consumes = "application/json", produces = "application/json")
    ResponseEntity<List<ClientDto>> createClient(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody List<ClientDto> clientDtos);

    @GetMapping(value = "/auth/client", consumes = "application/json", produces = "application/json")
    ResponseEntity<ClientDto> findClientByInn(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestParam(value = "inn") String inn);

    @GetMapping(LOGIN)
    void login();
}
