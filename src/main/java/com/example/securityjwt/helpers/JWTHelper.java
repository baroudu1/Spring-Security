package com.example.securityjwt.helpers;

import com.example.securityjwt.security.UserAuth;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JWTHelper {

    @Value("${project.Auth.JWT.SecretKey}")
    private String SECRET;

    @Value("${project.Auth.JWT.Expiration}")
    private long EXPIRATION_TIME;

    public static final String TOKEN_PREFIX = "Bearer ";
    private String HEADER_STRING = "Authorization";

    private static final Logger logger = LoggerFactory.getLogger(JWTHelper.class);


    public String generateJWT(Authentication authentication) {

        UserAuth userPrincipal = (UserAuth) authentication.getPrincipal();
        List<String> listAuthorities = new ArrayList<>();

        userPrincipal.getAuthorities().forEach(
                role -> {
                    listAuthorities.add(role.getAuthority());
                }
        );


        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("id", userPrincipal.getId())
                .claim("username", userPrincipal.getUsername())
                .claim("firstName", userPrincipal.getFirstName())
                .claim("lastName", userPrincipal.getLastName())
                .claim("email", userPrincipal.getEmail())
                .claim("authorities", listAuthorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

    }

    public String getUsernameFromJWT(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }catch (Exception e) {
            logger.error("We've found an error while parsing JWT token: {}", e.getMessage());
        }
        return false;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader(HEADER_STRING);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(TOKEN_PREFIX)) {
            return headerAuth.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

}
