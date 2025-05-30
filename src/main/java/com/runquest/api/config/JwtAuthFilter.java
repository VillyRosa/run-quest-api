package com.runquest.api.config;

import com.runquest.api.domain.auth.JwtService;
import com.runquest.api.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        UUID userId;

        try {
            userId = jwtService.extractUserId(jwt);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String scope = jwtService.extractScope(jwt);
        String path = request.getRequestURI();

        if (path.startsWith("/auth/reset-password/confirm") && !"RESET_PASSWORD".equals(scope)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (!path.startsWith("/auth") && "RESET_PASSWORD".equals(scope)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        var authToken = new UsernamePasswordAuthenticationToken(userId, null, null);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
