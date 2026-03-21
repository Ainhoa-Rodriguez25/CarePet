package com.tfg.carepet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Indica a Spring que se trata de una clase de configuración
@EnableWebSecurity // Habilita configuración de seguridad web
public class SecurityConfig {

    @Bean // Se crea objeto de tipo PasswordEncoder par encriptar las contraseñas
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Aquí se configuran las reglas de seguridad de la aplicación y qué endpoints serán públicos y cuáles no
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Se deshabilita la protección de aplicaciones tradicionales con sesiones
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No se crean sesiones en el servidor (uso JWT)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .anyRequest().authenticated()
                ); // Se indican qué peticiones pueden ser con autenticación y cuáles no

        return http.build(); // Devuelve la configuración de seguridad construida
    }
}
