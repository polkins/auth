package ru.nonsense.auth.manager;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.nonsense.auth.repository.UserRepository;
import ru.nonsense.auth.service.UserDetailsServiceImpl;

import java.util.List;

@Data
@Slf4j
@Component
public class AuthenticationManagerImpl implements AuthenticationManager {

    private final UserDetailsServiceImpl detailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UserDetails userDetails = detailsService.loadUserByUsername(login);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User was not found");
        }

        String storedPassword = userDetails.getPassword();

        if (!passwordEncoder.matches(password, storedPassword)) {
            throw new BadCredentialsException("User was not found");
        }

        return new UsernamePasswordAuthenticationToken(login, password, List.of());
    }
}
