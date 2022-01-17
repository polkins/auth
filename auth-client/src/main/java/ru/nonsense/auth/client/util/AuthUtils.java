package ru.nonsense.auth.client.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthUtils {
    public static final String API_PREFIX = "/";
    public static final String AUTH = "auth";
    public static final String CREATE_USERS = API_PREFIX + AUTH + API_PREFIX + "createUsers";
    public static final String CREATE_CLIENTS = API_PREFIX + AUTH + API_PREFIX + "createClients";
    public static final String TOKEN = API_PREFIX + AUTH + API_PREFIX + "token";
    public static final String LOGIN = API_PREFIX + AUTH + "/login";
}
