package ru.nonsense.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ClientDto {
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("Имя")
    private String firstName;

    @ApiModelProperty("Отчество")
    private String lastName;

    @ApiModelProperty("Фамилия")
    private String surName;

    @ApiModelProperty("Юзеры")
    private List<UserDto> users;
}
