package controller;

import java.util.List;
import model.Cultivo;
import model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.CultivoService;
import service.UsuarioService;

/**
 *
 * @author josue
 */
@RestController
@RequestMapping("/api/cultivos")
@CrossOrigin(origins = "*")
public class CultivoController {

    @Autowired
    private CultivoService cultivoService;
    @Autowired
    private UsuarioService usuarioService;

    // Obtener lista de todos los cultivos configurados
    @GetMapping
    public List<Cultivo> listar() {
        return cultivoService.obtenerTodos();
    }

    // Obtener detalle de un cultivo específico
    @GetMapping("/{id}")
    public ResponseEntity<Cultivo> buscarPorId(@PathVariable Integer id) {
        return cultivoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Registrar un nuevo tipo de cultivo (ej. Tomate, Lechuga)
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Cultivo cultivo) {
        Usuario usuarioRequest = cultivo.getUsuario();
        if (usuarioRequest == null || usuarioRequest.getIdUsuario() == null) {
            return ResponseEntity.badRequest().body("El usuario es requerido para crear el cultivo.");
        }

        Usuario usuarioExistente = usuarioService.obtenerPorId(usuarioRequest.getIdUsuario())
                .orElse(null);
        if (usuarioExistente == null) {
            return ResponseEntity.badRequest().body("El usuario indicado no existe. Verifica la sesion iniciada.");
        }

        cultivo.setUsuario(usuarioExistente);
        Cultivo nuevo = cultivoService.guardar(cultivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // Actualizar parámetros de un cultivo existente
    @PutMapping("/{id}")
    public ResponseEntity<Cultivo> actualizar(@PathVariable Integer id, @RequestBody Cultivo cultivo) {
        if (!cultivoService.obtenerPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        cultivo.setIdCultivo(id); // Aseguramos que se actualice el correcto
        return ResponseEntity.ok(cultivoService.guardar(cultivo));
    }

    // Eliminar un cultivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        cultivoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
