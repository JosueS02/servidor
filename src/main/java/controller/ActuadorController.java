package controller;

import java.util.List;
import java.util.Map;
import model.InvernaderoActuador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.ActuadorService;

/**
 *
 * @author josue
 */
@RestController
@RequestMapping("/api/actuadores")
@CrossOrigin(origins = "*")
public class ActuadorController {

    @Autowired
    private ActuadorService actuadorService;

    // Listar actuadores de un invernadero
    @GetMapping("/invernadero/{id}")
    public List<InvernaderoActuador> listar(@PathVariable Integer id) {
        return actuadorService.listarPorInvernadero(id);
    }

    // Endpoint para encender o apagar (Acción manual del usuario)
    // POST http://localhost:8080/api/actuadores/control/1
    // Body: { "estado": true }
    @PostMapping("/control/{idInvActuador}")
    public ResponseEntity<?> controlar(@PathVariable Integer idInvActuador, @RequestBody Map<String, Boolean> body) {
        Boolean estado = body.get("estado");
        actuadorService.cambiarEstado(idInvActuador, estado);
        return ResponseEntity.ok().body("Comando procesado correctamente");
    }
}
