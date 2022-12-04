package com.example.ebook.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    private static final String LOGIN_URL = "/member/login";
    private static final String LOGOUT_URL = "/member/logout";



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .httpBasic().disable()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                .and()
                .formLogin()
                .loginPage(LOGIN_URL)
                .loginProcessingUrl(LOGIN_URL)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)

                .and()
                .logout()
                .logoutUrl(LOGOUT_URL)
                .logoutSuccessUrl(LOGIN_URL)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "JWT-SESSION")

                .and()
                .authorizeRequests()
                .mvcMatchers("/", "/error/**", "/js/**", "/css/**", "/image/**").permitAll()
                .antMatchers("/member/join", "/member/login", "/member/findPassword", "/member/findUsername").permitAll()
                .antMatchers("/product/create").hasRole("WRITER")
                .antMatchers("/member/**").hasRole("USER")
                .anyRequest().authenticated();

        return httpSecurity.build();
    }
}
