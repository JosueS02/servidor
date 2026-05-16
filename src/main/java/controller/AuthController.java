package controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.JwtUtil;
import service.UserDetailsServiceImpl;

/**
 *
 * @author josue
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String, String> credentials) throws Exception {
        try {
            // Intentamos loguear al usuario
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.get("correo"), credentials.get("contrasena"))
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Correo o contraseña incorrectos");
        }

        // Si pasó, generamos el token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.get("correo"));
        final String jwt = jwtUtil.generateToken(userDetails);

        // Devolvemos el token al frontend
        return ResponseEntity.ok(Map.of("token", jwt));
    }
}
