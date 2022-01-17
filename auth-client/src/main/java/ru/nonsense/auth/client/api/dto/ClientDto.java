package ru.nonsense.auth.client.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ClientDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String surName;
    private String inn;
    private List<UserDto> users;
}
