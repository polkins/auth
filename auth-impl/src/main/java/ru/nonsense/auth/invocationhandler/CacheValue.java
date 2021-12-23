package ru.nonsense.auth.invocationhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class CacheValue {
    private final Object data;
    private final LocalDateTime expireTime;
}
