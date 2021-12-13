package ru.nonsense.auth.domain.entity.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import ru.nonsense.auth.domain.entity.client.Client;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
@Table(name = "users")
public class AuthUser {

    @Id
    @GenericGenerator(
            name = "ID_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "sequence_name", value = "users_sequence")
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_GENERATOR")
    private Long id;

    private String login;

    private String password;

    private String firstName;

    private String lastName;

    private String surName;

    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_client",
            joinColumns = { @JoinColumn(name = "client_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id"), }
    )
    public List<Client> clients = new ArrayList<>();
}
