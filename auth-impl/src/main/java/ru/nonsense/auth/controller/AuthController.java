package ru.nonsense.auth.controller;

import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.nonsense.api.dto.ClientDto;
import ru.nonsense.api.dto.UserDto;
import ru.nonsense.auth.domain.entity.user.AuthUser;
import ru.nonsense.auth.mapper.AuthClientDtoMapper;
import ru.nonsense.auth.repository.ClientRepository;
import ru.nonsense.auth.service.AuthUserService;
import ru.nonsense.auth.utils.JWTUtil;

import java.util.List;
import java.util.Optional;

import static ru.nonsense.auth.utils.AuthUtils.API_PREFIX;
import static ru.nonsense.auth.utils.AuthUtils.AUTH;
import static ru.nonsense.auth.utils.AuthUtils.CREATE_CLIENTS;
import static ru.nonsense.auth.utils.AuthUtils.CREATE_USERS;
import static ru.nonsense.auth.utils.AuthUtils.TOKEN;

@Data
@RestController
@RequestMapping(API_PREFIX + AUTH)
public class AuthController {
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ClientRepository clientRepository;
    private final AuthClientDtoMapper authClientDtoMapper;
    private final AuthUserService authUserService;

    //TODO: создать сервисы!

    @PostMapping(TOKEN)
    @ApiOperation(value = "Get token for user")
    public ResponseEntity<String> getJwtToken(@RequestParam(value = "username") String username,
                                              @RequestParam(value = "password") String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        Authentication returnedAuthentication = authenticationManager.authenticate(authentication);
        AuthUser user = authUserService.findByLogin((String) returnedAuthentication.getPrincipal());

        // для ролевой
//        String authorities = (String) returnedAuthentication.getAuthorities().toString();

        if (user != null) {
            var fio = user.getFirstName() + user.getSurName() + user.getLastName();
            String jwt = jwtUtil.createJWT(fio, user.getClients());
            return ResponseEntity.of(Optional.of(jwt));
        }

        return ResponseEntity.of(Optional.empty());
    }

    @PostMapping(CREATE_USERS)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create Users")
    public ResponseEntity<List<UserDto>> createUser(@RequestBody List<UserDto> userDtos) {
        return ResponseEntity.ok(authUserService.createUsers(userDtos));

    }

    @PostMapping(CREATE_CLIENTS)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create clients")
    public ResponseEntity<List<ClientDto>> createClient(@RequestBody List<ClientDto> clientDtos) {
        return ResponseEntity.ok(authClientDtoMapper.toDto(clientRepository.saveAll(authClientDtoMapper.toDomainModel(clientDtos))));
    }

    @GetMapping(value = "/client")
    @ApiOperation(value = "Find client by inn")
    public ResponseEntity<ClientDto> findClientByInn(@RequestParam(value = "inn") String inn){
        return ResponseEntity.ok(authClientDtoMapper.toDto(clientRepository.findByInn(inn)));
    }

    @GetMapping("/hello")
    @ApiOperation(value = "Check jwt")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello, you are authorized");
    }
}
