package met.petar_djordjevic_5594.gamevalut_server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF konfiguracija
                .csrf(csrf -> csrf.disable())

                // CORS konfiguracija
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Session management - STATELESS za JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Autorizacija zahteva
                .authorizeHttpRequests(auth -> auth
                        // Javni endpoint-i
                        .requestMatchers("/login", "/register").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        // Admin-only endpoint-i (POST metode)
                        .requestMatchers(HttpMethod.POST, "/games").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/games/genres").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/games/{gameId}/genres/{genreId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/games/publish/{gameId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/games/{gameId}/system-requirements").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/games/{gameId}/image").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/games/unpublished").hasRole("ADMIN")

                        // Sve ostale rute zahtevaju autentifikaciju (USER ili ADMIN)
                        .anyRequest().authenticated()
                )

                // Dodavanje custom JWT filter-a
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "http://localhost:4200", "http://localhost:8080","http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}