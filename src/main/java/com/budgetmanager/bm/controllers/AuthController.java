package com.budgetmanager.bm.controllers;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.budgetmanager.bm.domain.dtos.AuthRequest;
import com.budgetmanager.bm.domain.entities.RefreshToken;
import com.budgetmanager.bm.domain.entities.User;
import com.budgetmanager.bm.exceptions.GlobalException;
import com.budgetmanager.bm.security.JwtUtil;
import com.budgetmanager.bm.services.AuthService;
import com.budgetmanager.bm.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private static final String REFRESH_COOKIE_NAME = "refresh_token";
    private static final String ACCESS_COOKIE_NAME = "access_token";
    private final AuthService authService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refresh-expiration}")
    private int refreshExpiration;

    @Value("${jwt.access-expiration}")
    private int accessExpiration;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
        @RequestBody AuthRequest authRequest,
        HttpServletResponse response
    ) {
        String accessToken = authService.loginAndCreateAccessToken(
            authRequest.email(),
            authRequest.password()
        );
        RefreshToken refreshToken = userService
            .findByEmail(authRequest.email())
            .map(authService::createRefreshToken)
            .orElseThrow(() ->
                new GlobalException(
                    HttpStatus.NOT_FOUND,
                    "User not found for {}",
                    authRequest.email()
                )
            );

        cookieHelper(
            response,
            new Cookie(REFRESH_COOKIE_NAME, refreshToken.getToken()),
            refreshExpiration
        );
        cookieHelper(
            response,
            new Cookie(ACCESS_COOKIE_NAME, accessToken),
            accessExpiration
        );

        return ok(Map.of("message", "Logged in"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (REFRESH_COOKIE_NAME.equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            return status(HttpStatus.UNAUTHORIZED).body("No refresh token");
        }
        if (!authService.validateRefreshToken(refreshToken)) {
            return status(HttpStatus.UNAUTHORIZED).body(
                "Invalid/expired refresh token"
            );
        }
        RefreshToken newRefreshToken = authService.rotateRefreshToken(
            refreshToken
        );
        User user = newRefreshToken.getUser();
        String newAccess = jwtUtil.generateAccessToken(
            user.getUsername(),
            user
                .getRoles()
                .stream()
                .map(role -> role.getRoleName().toString())
                .toList()
        );

        cookieHelper(
            response,
            new Cookie(REFRESH_COOKIE_NAME, newRefreshToken.getToken()),
            refreshExpiration
        );
        cookieHelper(
            response,
            new Cookie(ACCESS_COOKIE_NAME, newAccess),
            accessExpiration
        );

        return ok(Map.of("message", "Refreshed"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            return status(HttpStatus.UNAUTHORIZED).body(
                "No authenticated user"
            );
        }
        userService
            .findByEmail(authentication.getName())
            .ifPresent(authService::revokeRefreshTokenForUser);

        cookieHelper(response, new Cookie(REFRESH_COOKIE_NAME, ""), 0);
        cookieHelper(response, new Cookie(ACCESS_COOKIE_NAME, ""), 0);

        return ok(Map.of("message", "Logged out"));
    }

    private void cookieHelper(
        HttpServletResponse response,
        Cookie cookie,
        int maxAge
    ) {
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge / 1000);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
