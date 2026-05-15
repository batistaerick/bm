package com.budgetmanager.bm.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.budgetmanager.bm.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String ACCESS_COOKIE_NAME = "access_token";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return (
            "/auth/login".equals(path) ||
            "/auth/refresh".equals(path) ||
            "/auth/logout".equals(path) ||
            "/auth/csrf".equals(path)
        );
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest req,
        @NonNull HttpServletResponse res,
        @NonNull FilterChain chain
    ) throws ServletException, IOException {
        String header = req.getHeader("Authorization");
        String token = null;

        if (header != null && header.startsWith(BEARER_PREFIX)) {
            token = header.substring(BEARER_PREFIX.length());
        } else if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if (ACCESS_COOKIE_NAME.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token != null) {
            try {
                DecodedJWT decoded = jwtUtil.validateAccessToken(token);
                String username = decoded.getSubject();

                if (
                    username != null &&
                    SecurityContextHolder.getContext().getAuthentication() ==
                    null
                ) {
                    UserDetails userDetails =
                        customUserDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );
                    authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(req)
                    );

                    var context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);
                }
            } catch (Exception exception) {
                SecurityContextHolder.clearContext();
                log.warn("Token invalid/expired: {}", exception.getMessage());
                res.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid or expired access token"
                );
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
