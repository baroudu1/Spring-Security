package com.example.securityjwt.security.filters;


import com.example.securityjwt.helpers.JWTHelper;
import com.example.securityjwt.security.UserDetailsServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomFilter extends OncePerRequestFilter {

    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(CustomFilter.class);

    private final RequestMatcher ignoredPaths = new AntPathRequestMatcher("/api/v1/auth/**");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (this.ignoredPaths.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = jwtHelper.getJwtFromRequest(request);
            if (jwtHelper.validateJwtToken(jwt) && jwt != null) {

                String username = jwtHelper.getUsernameFromJWT(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.error("JWT is not valid");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
                return;
            }
        }catch (Exception e){
            logger.error("JWT Exception: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
            return;
        }
        filterChain.doFilter(request, response);
    }


}
