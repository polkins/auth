package ru.nonsense.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nonsense.auth.domain.entity.client.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByInn(String inn);
}
