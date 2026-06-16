package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import model.Invernadero;
import model.Usuario;
import model.CatalogoSensor;
import model.CatalogoActuador;
import model.InvernaderoSensor;
import model.InvernaderoActuador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty; // Importante para blindar los nombres del JSON

import repository.CatalogoActuadorRepository;
import repository.CatalogoSensorRepository;
import repository.InvernaderoActuadorRepository;
import repository.InvernaderoSensorRepository;
import service.InvernaderoService;
import service.UsuarioService;

@RestController
@RequestMapping({"/api/invernaderos", "/api/greenhouses"})
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "X-Total-Count")
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

    // CLASE INTERNA DTO COMPLETAMENTE BLINDADA CON GETTERS, SETTERS Y JSONPROPERTY
    public static class InvernaderoDTO {
        @JsonProperty("idInvernadero")
        private String idInvernadero;

        @JsonProperty("idUsuario")
        private String idUsuario;

        @JsonProperty("nombre")
        private String nombre;

        @JsonProperty("ubicacion")
        private String ubicacion;

        @JsonProperty("estado")
        private String estado;

        @JsonProperty("nombresSensor")
        private List<String> nombresSensor;

        @JsonProperty("nombresActuador")
        private List<String> nombresActuador;

        public InvernaderoDTO(Invernadero inv) {
            this.idInvernadero = String.valueOf(inv.getIdInvernadero());
            this.idUsuario = inv.getUsuario() != null ? String.valueOf(inv.getUsuario().getIdUsuario()) : null;
            this.nombre = inv.getNombre();
            this.ubicacion = inv.getUbicacion();
            this.estado = inv.getEstado();
            
            this.nombresSensor = inv.getInvernaderoSensores() != null ? 
                inv.getInvernaderoSensores().stream()
                    .map(is -> is.getSensor().getNombre())
                    .collect(Collectors.toList()) : new ArrayList<>();
                    
            this.nombresActuador = inv.getInvernaderoActuadores() != null ? 
                inv.getInvernaderoActuadores().stream()
                    .map(ia -> ia.getActuador().getNombre())
                    .collect(Collectors.toList()) : new ArrayList<>();
        }

        // Getters y Setters explícitos para asegurar la correcta serialización de Jackson
        public String getIdInvernadero() { return idInvernadero; }
        public void setIdInvernadero(String idInvernadero) { this.idInvernadero = idInvernadero; }

        public String getIdUsuario() { return idUsuario; }
        public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getUbicacion() { return ubicacion; }
        public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }

        public List<String> getNombresSensor() { return nombresSensor; }
        public void setNombresSensor(List<String> nombresSensor) { this.nombresSensor = nombresSensor; }

        public List<String> getNombresActuador() { return nombresActuador; }
        public void setNombresActuador(List<String> nombresActuador) { this.nombresActuador = nombresActuador; }
    }

    @GetMapping
    public ResponseEntity<List<InvernaderoDTO>> listarTodos(@RequestParam(value = "userId", required = false) Integer userId) {
        List<Invernadero> todos = invernaderoService.obtenerTodos();
        
        if (userId != null) {
            todos = todos.stream()
                .filter(inv -> inv.getUsuario() != null && userId.equals(inv.getUsuario().getIdUsuario()))
                .collect(Collectors.toList());
        }

        List<InvernaderoDTO> dtos = todos.stream()
            .map(InvernaderoDTO::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(dtos.size()))
                .body(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvernaderoDTO> obtenerPorId(@PathVariable Integer id) {
        Optional<Invernadero> invernadero = invernaderoService.obtenerPorId(id);
        return invernadero.map(inv -> ResponseEntity.ok(new InvernaderoDTO(inv)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> payload) {
        String nombre = (String) payload.get("nombre");
        String ubicacion = (String) payload.get("ubicacion");
        String estado = (String) payload.get("estado");
        
        Object idUsuarioObj = payload.get("idUsuario");
        if (idUsuarioObj == null && payload.containsKey("usuario")) {
            Map<?, ?> usrMap = (Map<?, ?>) payload.get("usuario");
            idUsuarioObj = usrMap.get("idUsuario");
        }

        if (idUsuarioObj == null) {
            return ResponseEntity.badRequest().body("El campo idUsuario es requerido para crear el invernadero.");
        }

        Integer idUsuario = Integer.parseInt(idUsuarioObj.toString());
        Usuario usuarioExistente = usuarioService.obtenerPorId(idUsuario).orElse(null);
        
        if (usuarioExistente == null) {
            return ResponseEntity.badRequest().body("El usuario indicado no existe. Verifica la sesión iniciada.");
        }

        Invernadero invernadero = new Invernadero();
        invernadero.setNombre(nombre);
        invernadero.setUbicacion(ubicacion);
        invernadero.setEstado(estado);
        invernadero.setUsuario(usuarioExistente);

        Invernadero nuevoInvernadero = invernaderoService.guardar(invernadero);
        return ResponseEntity.status(HttpStatus.CREATED).body(new InvernaderoDTO(nuevoInvernadero));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Map<String, Object> payload) {
        Optional<Invernadero> existente = invernaderoService.obtenerPorId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Invernadero invernadero = existente.get();

        Object idUsuarioObj = payload.get("idUsuario");
        if (idUsuarioObj == null && payload.containsKey("usuario")) {
            Map<?, ?> usrMap = (Map<?, ?>) payload.get("usuario");
            idUsuarioObj = usrMap.get("idUsuario");
        }

        if (idUsuarioObj != null) {
            Integer idUsuario = Integer.parseInt(idUsuarioObj.toString());
            Usuario usuarioExistente = usuarioService.obtenerPorId(idUsuario).orElse(null);
            if (usuarioExistente == null) {
                return ResponseEntity.badRequest().body("El usuario indicado no existe. Verifica la sesión iniciada.");
            }
            invernadero.setUsuario(usuarioExistente);
        }

        if (payload.get("nombre") != null) {
            String nombre = payload.get("nombre").toString();
            if (!nombre.trim().isEmpty()) {
                invernadero.setNombre(nombre);
            }
        }

        if (payload.get("ubicacion") != null) {
            String ubicacion = payload.get("ubicacion").toString();
            if (!ubicacion.trim().isEmpty()) {
                invernadero.setUbicacion(ubicacion);
            }
        }

        if (payload.get("estado") != null) {
            invernadero.setEstado(payload.get("estado").toString());
        }

        Invernadero actualizado = invernaderoService.guardar(invernadero);
        return ResponseEntity.ok(new InvernaderoDTO(actualizado));
    }

    @PostMapping("/{id}/sensores")
    public ResponseEntity<?> agregarSensores(@PathVariable Integer id, @RequestBody Map<String, List<String>> body) {
        List<String> sensores = body.get("sensores");
        if (sensores == null) return ResponseEntity.badRequest().body("Campo 'sensores' es requerido");

        Optional<Invernadero> opt = invernaderoService.obtenerPorId(id);
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

        return ResponseEntity.ok().body(Map.of("message", "Sensores agregados con éxito"));
    }

    @PostMapping("/{id}/actuadores")
    public ResponseEntity<?> agregarActuadores(@PathVariable Integer id, @RequestBody Map<String, List<String>> body) {
        List<String> actuadores = body.get("actuadores");
        if (actuadores == null) return ResponseEntity.badRequest().body("Campo 'actuadores' es requerido");

        Optional<Invernadero> opt = invernaderoService.obtenerPorId(id);
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
            invAct.setEstadoOperativo("OFF");
            invernaderoActuadorRepository.save(invAct);
        }

        return ResponseEntity.ok().body(Map.of("message", "Actuadores agregados con éxito"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        invernaderoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}