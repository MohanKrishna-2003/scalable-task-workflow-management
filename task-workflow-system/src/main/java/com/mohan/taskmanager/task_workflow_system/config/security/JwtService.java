package com.mohan.taskmanager.task_workflow_system.config.security;

import com.mohan.taskmanager.task_workflow_system.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;


@Service
public class JwtService {

    // this JwtService code generates a secure JWT token for authenticated users.

//    Cryptographic signing key that is secret
//    Used for:
//    signing JWT
//    validating JWT
    private final JwtProperties jwtProperties;


    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getUserId().toString())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.accessExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token){
        return String.valueOf(
                extractAllClaims(token).getSubject()
        );
    }

    public boolean isTokenValid(String token){
        try {
            extractAllClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
