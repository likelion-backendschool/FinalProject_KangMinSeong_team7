package com.example.ebook.global.config.security;

import com.example.ebook.global.config.jwt.JwtAuthenticationFilter;
import com.example.ebook.global.config.jwt.JwtTokenProvider;
import com.example.ebook.global.config.web.CorsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .antMatcher("/api/**")
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/*/member/login").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin().disable()
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .logout().disable();

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
