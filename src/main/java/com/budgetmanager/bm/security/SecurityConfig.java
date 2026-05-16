package com.budgetmanager.bm.security;

import static org.springframework.security.config.Customizer.withDefaults;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Value("${cors.frontend}")
    private String url;

    @Bean
    public WebMvcConfigurer corsConfig() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry
                    .addMapping("/**")
                    .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                    .allowedHeaders(
                        "Authorization",
                        "Content-Type",
                        "X-CSRF-Token",
                        "X-Requested-With",
                        "X-XSRF-TOKEN"
                    )
                    .allowedOrigins(url)
                    .allowCredentials(true);
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .cors(withDefaults())
            .csrf(csrf ->
                csrf
                    .csrfTokenRepository(csrfTokenRepository())
                    .ignoringRequestMatchers(
                        PathPatternRequestMatcher.pathPattern("/auth/login"),
                        PathPatternRequestMatcher.pathPattern("/auth/refresh"),
                        PathPatternRequestMatcher.pathPattern("/auth/logout"),
                        PathPatternRequestMatcher.pathPattern("/auth/csrf"),
                        PathPatternRequestMatcher.pathPattern(
                            HttpMethod.POST,
                            "/users"
                        )
                    )
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authorization ->
                authorization
                    .requestMatchers("/auth/login")
                    .permitAll()
                    .requestMatchers("/auth/refresh")
                    .permitAll()
                    .requestMatchers("/auth/logout")
                    .permitAll()
                    .requestMatchers("/auth/csrf")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/users")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            )
            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository =
            CookieCsrfTokenRepository.withHttpOnlyFalse();

        repository.setCookieName("csrf_token");
        repository.setHeaderName("X-CSRF-Token");

        return repository;
    }
}
