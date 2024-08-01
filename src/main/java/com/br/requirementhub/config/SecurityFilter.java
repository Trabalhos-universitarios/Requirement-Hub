package com.br.requirementhub.config;

import com.br.requirementhub.enums.Permission;
import com.br.requirementhub.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilter {
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionMangConfig -> sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authConfig -> {
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
                    authConfig.requestMatchers("/error").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/project/**").hasRole("GERENTE_DE_PROJETOS");
                    authConfig.anyRequest().authenticated();

                });

        return http.build();

    }
}


