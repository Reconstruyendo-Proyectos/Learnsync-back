package com.recpro.pe.learnsync.config;

import com.recpro.pe.learnsync.services.auth.JwtDetailsService;
import com.recpro.pe.learnsync.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // To create AuthenticationManager
    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @Autowired private JwtUtils jwtUtils;

    // Configure the security
    // HttpSecurity is very important
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.
                csrf(AbstractHttpConfigurer::disable)
                // Permit request to all endpoints of AuthController without authentication
                .authorizeHttpRequests(auth -> {
                    // Configure public endpoints
                    auth.requestMatchers(HttpMethod.GET, "/auth/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                    auth.requestMatchers(HttpMethod.PATCH, "/auth/**").permitAll();
                    auth.requestMatchers(HttpMethod.OPTIONS, "/auth/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/assets/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/topic/**", "/thread/**", "/category/**", "/comment/**").permitAll();
                    auth.requestMatchers("/doc/**", "/v3/api-docs/**").permitAll();
                    auth.requestMatchers("/h2-console").permitAll();
                    // Configure the others endpoints
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    // Managment the authentication
    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Bring users from DB
    @Bean
    public AuthenticationProvider authenticationProvider(JwtDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    // Encrypt password in DB
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}