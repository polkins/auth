package ru.nonsense.auth.common.query;

public class QueryParam {

    private final String name;
    private final Object value;

    public static QueryParam of(String name, Object value) {
        return new QueryParam(name, value);
    }

    private QueryParam(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
