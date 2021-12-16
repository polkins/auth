package ru.nonsense.auth.controller;

import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.nonsense.api.dto.ClientDto;
import ru.nonsense.api.dto.UserDto;
import ru.nonsense.auth.domain.entity.client.Client;
import ru.nonsense.auth.domain.entity.user.AuthUser;
import ru.nonsense.auth.mapper.AuthClientDtoMapper;
import ru.nonsense.auth.mapper.AuthUserDtoMapper;
import ru.nonsense.auth.repository.ClientRepository;
import ru.nonsense.auth.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final AuthUserDtoMapper authUserDtoMapper;
    private final AuthClientDtoMapper authClientDtoMapper;
    private final PasswordEncoder passwordEncoder;

    //TODO: создать сервисы!

    @PostMapping(TOKEN)
    @ApiOperation(value = "Get token for user")
    public ResponseEntity<String> getJwtToken(@RequestParam(value = "username") String username,
                                              @RequestParam(value = "password") String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        Authentication returnedAuthentication = authenticationManager.authenticate(authentication);
        AuthUser user = userRepository.findByLogin((String) returnedAuthentication.getPrincipal());

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
    public ResponseEntity<List<AuthUser>> createUser(@RequestBody List<UserDto> userDtos) {
        userDtos.forEach(u -> u.setPassword(passwordEncoder.encode(u.getPassword())));
        return ResponseEntity.of(Optional.of(userRepository.saveAll(authUserDtoMapper.toDomainModel(userDtos))));

    }

    @PostMapping(CREATE_CLIENTS)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create clients")
    public ResponseEntity<List<Client>> createClient(@RequestBody List<ClientDto> clientDtos) {
        return ResponseEntity.of(Optional.of(clientRepository.saveAll(authClientDtoMapper.toDomainModel(clientDtos))));

    }

    @GetMapping("/hello")
    @ApiOperation(value = "Check jwt")
    public ResponseEntity<String> hello() {
        return ResponseEntity.of(Optional.of("Hello, you are authorized"));
    }
}
