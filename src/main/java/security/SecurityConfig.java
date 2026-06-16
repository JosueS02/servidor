package security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author josue
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Activa el CORS usando el bean global que creamos abajo
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(
                        "/api/usuarios/registro",
                        "/api/auth/login",
                        "/error",
                        "/api/simulation/**",
                        "/ws-invernadero/**"
                ).permitAll()
                .requestMatchers(
                        "/api/invernaderos", "/api/invernaderos/**",
                        "/api/cultivos", "/api/cultivos/**",
                        "/api/sensores", "/api/sensores/**",
                        "/api/actuadores", "/api/actuadores/**"
                ).permitAll()
                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://127.0.0.1:5173");
        config.addAllowedOrigin("http://localhost:5174");
        config.addAllowedOrigin("http://127.0.0.1:5174");
        config.addAllowedOrigin("http://localhost:4173");
        config.addAllowedOrigin("http://127.0.0.1:4173");
        config.addAllowedOrigin("http://172.20.10.2:5173");
        config.addAllowedHeader("*"); // Permite cualquier cabecera (incluyendo Authorization)
        config.addAllowedMethod("*"); // Permite GET, POST, PUT, DELETE, OPTIONS

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
