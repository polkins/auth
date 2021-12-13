package ru.nonsense.auth.service;

import lombok.Data;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nonsense.auth.domain.entity.user.AuthUser;
import ru.nonsense.auth.repository.UserRepository;

import java.util.List;

@Service
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String enteredUserName) throws UsernameNotFoundException {
        AuthUser authUser = userRepository.findByLogin(enteredUserName);

        if (authUser == null) {
            throw new UsernameNotFoundException(enteredUserName);
        }

        String storedPassword = authUser.getPassword();

//        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));

        return  new User(enteredUserName, storedPassword, List.of());
    }
}
