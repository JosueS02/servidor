package controller;

import java.util.Map;
import model.Usuario;
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
import service.UsuarioService; // Importación del servicio de usuarios

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
    private UsuarioService usuarioService; // Inyección de dependencia para recuperar los datos de sesión

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
            // Devolvemos un objeto JSON con propiedad 'message' estructurado para el parseApiError del front
            return ResponseEntity.status(401).body(Map.of("message", "Correo o contraseña incorrectos"));
        }

        // Si la autenticación es exitosa, generamos el token correspondiente
        final UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.get("correo"));
        final String jwt = jwtUtil.generateToken(userDetails);

        // Recuperamos el registro completo del usuario desde la base de datos para obtener su ID
        Usuario usuario = usuarioService.obtenerPorCorreo(credentials.get("correo")).orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(404).body(Map.of("message", "El usuario autenticado no existe en el sistema"));
        }

        // Devolvemos el token junto con las propiedades de identidad que el frontend espera almacenar
        return ResponseEntity.ok(Map.of(
            "token", jwt,
            "id_usuario", String.valueOf(usuario.getIdUsuario()),
            "idUsuario", String.valueOf(usuario.getIdUsuario()),
            "correo", usuario.getCorreo()
        ));
    }
}