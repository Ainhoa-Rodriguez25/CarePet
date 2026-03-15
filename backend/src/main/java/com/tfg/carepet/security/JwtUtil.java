package com.tfg.carepet.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component // Marca la clase como un componente de Spring
public class JwtUtil {

    @Value("${jwt.secret}") // Se utiliza para leer el valor de jwt.secret del archivo application.properties
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Generar clave de firma a partir del atributo secret (convertido en clave criptográfica)
    private Key getSigninKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generar token JWT
    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId); // Claims son los datos que contiene el token, se guarda el userId para identificar al usuario

        return Jwts.builder()
                .setClaims(claims) // Añade los datos
                .setSubject(userId.toString()) // Establece el sujeto del token
                .setIssuedAt(new Date()) // Establece fecha de creacion
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Establece una fecha de expiracion del token
                .signWith(getSigninKey(), SignatureAlgorithm.HS256) // Firma token con clave secreta
                .compact(); // Genera el token final como string
    }

    // Validar token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigninKey())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Obtener userId del token
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
}
