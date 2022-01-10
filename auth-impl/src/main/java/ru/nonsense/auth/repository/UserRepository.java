package ru.nonsense.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nonsense.auth.annotation.MyCacheable;
import ru.nonsense.auth.domain.entity.user.AuthUser;

public interface UserRepository extends JpaRepository<AuthUser, Long> {

    @MyCacheable(name = "users")
    AuthUser findByLogin(String login);
}
