package controller;

import java.util.List;
import java.util.Optional;
import model.Invernadero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.InvernaderoService;

/**
 *
 * @author josue
 */
@RestController
@RequestMapping("/api/invernaderos")
@CrossOrigin(origins = "*") // ¡Súper importante! Permite que tu Frontend se conecte sin errores de CORS
public class InvernaderoController {

    @Autowired
    private InvernaderoService invernaderoService;

    // GET: http://localhost:8080/api/invernaderos
    @GetMapping
    public ResponseEntity<List<Invernadero>> listarTodos() {
        return ResponseEntity.ok(invernaderoService.obtenerTodos());
    }

    // GET: http://localhost:8080/api/invernaderos/1
    @GetMapping("/{id}")
    public ResponseEntity<Invernadero> obtenerPorId(@PathVariable Integer id) {
        Optional<Invernadero> invernadero = invernaderoService.obtenerPorId(id);
        return invernadero.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST: http://localhost:8080/api/invernaderos (Para enviar JSON desde el Front y guardarlo)
    @PostMapping
    public ResponseEntity<Invernadero> crear(@RequestBody Invernadero invernadero) {
        Invernadero nuevoInvernadero = invernaderoService.guardar(invernadero);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInvernadero);
    }

    // DELETE: http://localhost:8080/api/invernaderos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        invernaderoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
