package ru.nonsense.auth;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.nonsense.auth.domain.entity.user.AuthUser;
import ru.nonsense.auth.repository.UserRepository;

import javax.transaction.Transactional;

@Data
@Component
public class UserLoader implements CommandLineRunner {

    @Value("${admin.login}")
    private String adminLogin;

    @Value("${admin.password}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        AuthUser authUser = new AuthUser()
                .setLogin(adminLogin)
                .setPassword(passwordEncoder.encode(adminPassword));

        userRepository.save(authUser);
    }
}
