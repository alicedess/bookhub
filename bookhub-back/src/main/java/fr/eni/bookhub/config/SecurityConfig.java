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
                    configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:8080"));
                    configuration.setAllowedMethods(List.of("*"));
                    configuration.setAllowedHeaders(List.of("*"));
                    return configuration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                // Books
                                .requestMatchers(HttpMethod.POST, "/api/books/**").hasAnyRole( "LIBRARIAN")
                                .requestMatchers(HttpMethod.PUT, "/api/books/**").hasAnyRole( "LIBRARIAN")
                                .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()

                                // Swagger
                                .requestMatchers(HttpMethod.GET, "/api/docs").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/docs/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                                .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()

                                // Categories
                                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                                // Auteurs
                                .requestMatchers(HttpMethod.GET, "/api/authors/**").permitAll()

                                // Authentication
                                .requestMatchers("/api/auth/**").permitAll() // Sur /auth, pas besoin d'authentification

                                .requestMatchers("/dashboard/**").hasAnyRole("ADMIN", "LIBRARIAN", "USER") // Les utilisateurs, libraires et les admins peuvent accéder à /books
                                .requestMatchers("/loans/**").hasAnyRole("ADMIN", "LIBRARIAN", "USER") // Les utilisateurs, libraires et les admins peuvent accéder à /books
                                .requestMatchers("/reservation/**").hasAnyRole("ADMIN", "LIBRARIAN", "USER") // Les utilisateurs, libraires et les admins peuvent accéder à /books
//                                .requestMatchers("/profile/**").hasAnyRole("ADMIN", "LIBRARIAN", "USER") // Les utilisateurs, libraires et les admins peuvent accéder à /books
                                .requestMatchers("/admin/**").hasRole("ADMIN") // Seuls les admins peuvent accéder à /admin
                                .requestMatchers("/librarian/**").hasAnyRole("ADMIN", "LIBRARIAN") // Les libraires et les admins peuvent accéder à /librarian

                                // Stats
                                .requestMatchers(HttpMethod.GET, "/api/stats/**").hasAnyRole("ADMIN", "LIBRARIAN")

                                // Profile
                                .requestMatchers(HttpMethod.GET, "/api/user/me").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/user/me").authenticated()
                                .requestMatchers("/api/user/**").hasAnyRole("ADMIN", "LIBRARIAN", "USER")
                                .requestMatchers("/api/auth/**").permitAll()

                                .anyRequest().authenticated()) // Pour le reste, il faut un token d'accès


                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}