package fr.eni.bookhub.config;

import fr.eni.bookhub.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("http://localhost:4200"));
                    configuration.setAllowedMethods(List.of("*"));
                    configuration.setAllowedHeaders(List.of("*"));
                    return configuration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(HttpMethod.GET, "/api/books/*/cover").permitAll()
                                .requestMatchers("/api/auth/**").permitAll() // Sur /auth, pas besoin d'authentification
                                .requestMatchers("/books/**").permitAll() // Les utilisateurs et les admins peuvent accéder à /books
                                .requestMatchers("/dashboard/**").hasAnyRole("ADMIN", "LIBRARIAN", "USER") // Les utilisateurs, libraires et les admins peuvent accéder à /books
                                .requestMatchers("/loans/**").hasAnyRole("ADMIN", "LIBRARIAN", "USER") // Les utilisateurs, libraires et les admins peuvent accéder à /books
                                .requestMatchers("/reservation/**").hasAnyRole("ADMIN", "LIBRARIAN", "USER") // Les utilisateurs, libraires et les admins peuvent accéder à /books
                                .requestMatchers("/profile/**").hasAnyRole("ADMIN", "LIBRARIAN", "USER") // Les utilisateurs, libraires et les admins peuvent accéder à /books
                                .requestMatchers("/admin/**").hasRole("ADMIN") // Seuls les admins peuvent accéder à /admin
                                .requestMatchers("/librarian/**").hasAnyRole("ADMIN", "LIBRARIAN") // Les libraires et les admins peuvent accéder à /librarian
                                .anyRequest().authenticated()) // Pour le reste, il faut un token d'accès
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}