package ru.nonsense.auth.common.query;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@NoArgsConstructor(staticName = "create")
public class QueryStringBuilder {

    private final List<QueryParam> params = new ArrayList<>();

    public QueryStringBuilder add(QueryParam param) {
        params.add(param);

        return this;
    }

    public String build() {
        String queryString = params.stream()
                .filter(param -> nonNull(param.getValue()))
                .map(Object::toString)
                .collect(Collectors.joining("&"));

        return StringUtils.isNotBlank(queryString) ? "?" + queryString : StringUtils.EMPTY;
    }
}
