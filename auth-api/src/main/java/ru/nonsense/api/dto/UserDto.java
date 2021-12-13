package ru.nonsense.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class UserDto {
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("Идентификатор имени юзера")
    private String login;

    @ApiModelProperty("Пароль")
    private String password;

    @ApiModelProperty("Имя")
    private String firstName;

    @ApiModelProperty("Отчество")
    private String lastName;

    @ApiModelProperty("Фамилия")
    private String surName;

    @ApiModelProperty("Клиенты")
    private List<ClientDto> clients;
}
