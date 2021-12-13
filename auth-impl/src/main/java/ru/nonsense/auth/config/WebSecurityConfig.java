package ru.nonsense.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.nonsense.auth.filter.FilterImpl;

import static ru.nonsense.auth.utils.AuthUtils.FULL_PATH_CREATE_CLIENTS;
import static ru.nonsense.auth.utils.AuthUtils.FULL_PATH_CREATE_TOKEN;
import static ru.nonsense.auth.utils.AuthUtils.FULL_PATH_CREATE_USERS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private FilterImpl filter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        httpSecurity.authorizeRequests().antMatchers(FULL_PATH_CREATE_TOKEN).permitAll();
        // TODO: добавить роли на эти запросы, только с ролью админа можно создавать юзеров, а клиента только с ролью юзера
        httpSecurity.authorizeRequests().antMatchers(FULL_PATH_CREATE_USERS).permitAll();
        httpSecurity.authorizeRequests().antMatchers(FULL_PATH_CREATE_CLIENTS).permitAll();

        httpSecurity.csrf().disable();
        httpSecurity.authorizeRequests().anyRequest().authenticated();
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.headers().frameOptions().sameOrigin();
    }
}
