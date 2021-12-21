package ru.nonsense.auth.invocationhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CacheValue {
    private final Object data;
    private final LocalDateTime expireTime;
}
