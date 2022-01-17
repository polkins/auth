package ru.nonsense.auth.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthUtils {
    public static final String API_PREFIX = "/";
    public static final String AUTH = "auth";
    public static final String CREATE_USERS = API_PREFIX + "createUsers";
    public static final String CREATE_CLIENTS = API_PREFIX + "createClients";
    public static final String FIND_CLIENT_BY_INN = API_PREFIX + "client";
    public static final String TOKEN = API_PREFIX + "token";
    public static final String FULL_PATH_CREATE_USERS = API_PREFIX + AUTH + CREATE_USERS;
    public static final String FULL_PATH_CREATE_CLIENTS = API_PREFIX + AUTH + CREATE_CLIENTS;
    public static final String FULL_PATH_CREATE_TOKEN = API_PREFIX + AUTH + TOKEN;
    public static final String LOGIN = API_PREFIX + AUTH + "/login";
}
