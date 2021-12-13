package ru.nonsense.auth.filter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.nonsense.auth.utils.JWTUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Component
public class FilterImpl implements Filter {
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null) {
            addAuthenticationObjectIntoContext(authorizationHeader);
        }
            chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private void addAuthenticationObjectIntoContext(String authorizationHeader) {
        String jwt = jwtUtil.extractJWTFromAuthorizationHeader(authorizationHeader);

        Claims claims = jwtUtil.getClaims(jwt);
        var principal =  claims.get("user");
        var credentials =  claims.get("clients");

        //AUTHENTICATE
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, credentials, List.of());
        //STORE AUTHENTICATION INTO CONTEXT (SESSION)
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
