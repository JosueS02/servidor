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
import repository.CatalogoActuadorRepository;
import repository.CatalogoSensorRepository;
import repository.InvernaderoActuadorRepository;
import repository.InvernaderoSensorRepository;
import model.CatalogoActuador;
import model.CatalogoSensor;
import model.InvernaderoActuador;
import model.InvernaderoSensor;
import model.Usuario;
import service.InvernaderoService;
import service.UsuarioService;

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
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private CatalogoSensorRepository catalogoSensorRepository;
    @Autowired
    private CatalogoActuadorRepository catalogoActuadorRepository;
    @Autowired
    private InvernaderoSensorRepository invernaderoSensorRepository;
    @Autowired
    private InvernaderoActuadorRepository invernaderoActuadorRepository;

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
    public ResponseEntity<?> crear(@RequestBody Invernadero invernadero) {
        Usuario usuarioRequest = invernadero.getUsuario();
        if (usuarioRequest == null || usuarioRequest.getIdUsuario() == null) {
            return ResponseEntity.badRequest().body("El usuario es requerido para crear el invernadero.");
        }

        Usuario usuarioExistente = usuarioService.obtenerPorId(usuarioRequest.getIdUsuario())
                .orElse(null);
        if (usuarioExistente == null) {
            return ResponseEntity.badRequest().body("El usuario indicado no existe. Verifica la sesion iniciada.");
        }

        invernadero.setUsuario(usuarioExistente);
        Invernadero nuevoInvernadero = invernaderoService.guardar(invernadero);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInvernadero);
    }

    // POST: http://localhost:8080/api/invernaderos/{id}/sensores
    // Body: { "sensores": ["Humedad","Temperatura"] }
    @PostMapping("/{id}/sensores")
    public ResponseEntity<?> agregarSensores(@PathVariable Integer id, @RequestBody java.util.Map<String, java.util.List<String>> body) {
        java.util.List<String> sensores = body.get("sensores");
        if (sensores == null) return ResponseEntity.badRequest().body("Campo 'sensores' es requerido");

        java.util.Optional<Invernadero> opt = invernaderoService.obtenerPorId(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Invernadero invernadero = opt.get();

        for (String nombre : sensores) {
            CatalogoSensor catalogo = catalogoSensorRepository.findByNombre(nombre).orElseGet(() -> {
                CatalogoSensor cs = new CatalogoSensor();
                cs.setNombre(nombre);
                cs.setUnidad("");
                return catalogoSensorRepository.save(cs);
            });

            InvernaderoSensor invSensor = new InvernaderoSensor();
            invSensor.setInvernadero(invernadero);
            invSensor.setSensor(catalogo);
            invSensor.setEstadoOperativo("ACTIVO");
            invernaderoSensorRepository.save(invSensor);
        }

        return ResponseEntity.ok().body("Sensores agregados");
    }

    // POST: http://localhost:8080/api/invernaderos/{id}/actuadores
    // Body: { "actuadores": ["Riego","Ventilador"] }
    @PostMapping("/{id}/actuadores")
    public ResponseEntity<?> agregarActuadores(@PathVariable Integer id, @RequestBody java.util.Map<String, java.util.List<String>> body) {
        java.util.List<String> actuadores = body.get("actuadores");
        if (actuadores == null) return ResponseEntity.badRequest().body("Campo 'actuadores' es requerido");

        java.util.Optional<Invernadero> opt = invernaderoService.obtenerPorId(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Invernadero invernadero = opt.get();

        for (String nombre : actuadores) {
            CatalogoActuador catalogo = catalogoActuadorRepository.findByNombre(nombre).orElseGet(() -> {
                CatalogoActuador ca = new CatalogoActuador();
                ca.setNombre(nombre);
                ca.setTipoAccion("");
                ca.setTiempoActivacion("");
                return catalogoActuadorRepository.save(ca);
            });

            InvernaderoActuador invAct = new InvernaderoActuador();
            invAct.setInvernadero(invernadero);
            invAct.setActuador(catalogo);
            invAct.setEstadoOperativo("ACTIVO");
            invernaderoActuadorRepository.save(invAct);
        }

        return ResponseEntity.ok().body("Actuadores agregados");
    }

    // DELETE: http://localhost:8080/api/invernaderos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        invernaderoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
