package ru.nonsense.auth.domain.entity.exception;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExceptionDto {
    private String message;
}
