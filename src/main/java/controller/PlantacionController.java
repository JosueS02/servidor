package controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import model.Cultivo;
import model.Invernadero;
import model.Plantacion;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.CultivoService;
import service.InvernaderoService;
import service.PlantacionService;
import service.UsuarioService;

@RestController
@RequestMapping({"/api/plantings", "/api/plantaciones"})
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PlantacionController {

    @Autowired
    private PlantacionService plantacionService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private InvernaderoService invernaderoService;

    @Autowired
    private CultivoService cultivoService;

    public static class PlantacionDTO {
        public String idPlantacion;
        public String idUsuario;
        public String idInvernadero;
        public String nombreInvernadero;
        public String idCultivo;
        public String nombreCultivo;
        public String fechaPlantado;
        public String fechaFinalizacion;
        public String estado;

        public PlantacionDTO(Plantacion plantacion) {
            this.idPlantacion = plantacion.getIdPlantacion() != null ? String.valueOf(plantacion.getIdPlantacion()) : null;
            this.idUsuario = plantacion.getInvernadero() != null && plantacion.getInvernadero().getUsuario() != null
                    ? String.valueOf(plantacion.getInvernadero().getUsuario().getIdUsuario())
                    : null;
            this.idInvernadero = plantacion.getInvernadero() != null && plantacion.getInvernadero().getIdInvernadero() != null
                    ? String.valueOf(plantacion.getInvernadero().getIdInvernadero())
                    : null;
            this.nombreInvernadero = plantacion.getInvernadero() != null ? plantacion.getInvernadero().getNombre() : null;
            this.idCultivo = plantacion.getCultivo() != null && plantacion.getCultivo().getIdCultivo() != null
                    ? String.valueOf(plantacion.getCultivo().getIdCultivo())
                    : null;
            this.nombreCultivo = plantacion.getCultivo() != null ? plantacion.getCultivo().getNombre() : null;
            this.fechaPlantado = plantacion.getFechaPlantado() != null ? plantacion.getFechaPlantado().toString() : null;
            this.fechaFinalizacion = plantacion.getFechaFinalizacion() != null ? plantacion.getFechaFinalizacion().toString() : null;
            this.estado = plantacion.getEstado();
        }
    }

    public static class PlantacionRequest {
        public String idUsuario;
        public String idInvernadero;
        public String idCultivo;
        public String fechaPlantado;
        public String fechaFinalizacion;
        public String estado;
    }

    public static class FinalizarPlantacionRequest {
        public String idUsuario;
        public String idInvernadero;
    }

    @GetMapping
    public ResponseEntity<List<PlantacionDTO>> listar(
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "status", required = false) String status) {
        if (userId == null) {
            return ResponseEntity.ok(List.of());
        }

        List<PlantacionDTO> dtos = plantacionService.listarPorUsuario(userId, status).stream()
                .map(PlantacionDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantacionDTO> obtenerPorId(@PathVariable Integer id) {
        return plantacionService.obtenerPorId(id)
                .map(plantacion -> ResponseEntity.ok(new PlantacionDTO(plantacion)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/active/{idInvernadero}")
    public ResponseEntity<PlantacionDTO> obtenerActivaPorInvernadero(@PathVariable Integer idInvernadero) {
        return plantacionService.obtenerActivaPorInvernadero(idInvernadero)
                .map(plantacion -> ResponseEntity.ok(new PlantacionDTO(plantacion)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PlantacionRequest request) {
        Plantacion plantacion = buildPlantacion(request, null);
        if (plantacion == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Datos invalidos para crear la plantacion"));
        }

        Plantacion guardada = plantacionService.guardar(plantacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PlantacionDTO(guardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody PlantacionRequest request) {
        if (plantacionService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Plantacion plantacion = buildPlantacion(request, id);
        if (plantacion == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Datos invalidos para actualizar la plantacion"));
        }

        Plantacion actualizada = plantacionService.guardar(plantacion);
        return ResponseEntity.ok(new PlantacionDTO(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> inactivar(@PathVariable Integer id) {
        return plantacionService.obtenerPorId(id)
                .map(plantacion -> {
                    plantacion.setEstado("INACTIVA");
                    if (plantacion.getFechaFinalizacion() == null) {
                        plantacion.setFechaFinalizacion(LocalDate.now());
                    }
                    Plantacion actualizada = plantacionService.guardar(plantacion);
                    return ResponseEntity.ok(new PlantacionDTO(actualizada));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/finalize")
    public ResponseEntity<?> finalizarActiva(@RequestBody FinalizarPlantacionRequest request) {
        Integer idUsuario = parseInteger(request != null ? request.idUsuario : null);
        Integer idInvernadero = parseInteger(request != null ? request.idInvernadero : null);

        if (idUsuario == null || idInvernadero == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Datos invalidos para finalizar la plantacion"));
        }

        return plantacionService.finalizarActivaPorInvernadero(idUsuario, idInvernadero)
                .map(plantacion -> ResponseEntity.ok(new PlantacionDTO(plantacion)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private Plantacion buildPlantacion(PlantacionRequest request, Integer idPlantacion) {
        if (request == null || isBlank(request.idUsuario) || isBlank(request.idInvernadero) || isBlank(request.idCultivo) || isBlank(request.fechaPlantado)) {
            return null;
        }

        Integer idUsuario = parseInteger(request.idUsuario);
        Integer idInvernadero = parseInteger(request.idInvernadero);
        Integer idCultivo = parseInteger(request.idCultivo);
        if (idUsuario == null || idInvernadero == null || idCultivo == null) {
            return null;
        }

        Usuario usuario = usuarioService.obtenerPorId(idUsuario).orElse(null);
        Invernadero invernadero = invernaderoService.obtenerPorId(idInvernadero).orElse(null);
        Cultivo cultivo = cultivoService.obtenerPorId(idCultivo).orElse(null);

        if (usuario == null || invernadero == null || cultivo == null) {
            return null;
        }

        if (invernadero.getUsuario() == null || !idUsuario.equals(invernadero.getUsuario().getIdUsuario())) {
            return null;
        }

        if (cultivo.getUsuario() == null || !idUsuario.equals(cultivo.getUsuario().getIdUsuario())) {
            return null;
        }

        Plantacion plantacion = new Plantacion();
        plantacion.setIdPlantacion(idPlantacion);
        plantacion.setInvernadero(invernadero);
        plantacion.setCultivo(cultivo);
        LocalDate fechaPlantado = parseDate(request.fechaPlantado);
        if (fechaPlantado == null) {
            return null;
        }

        plantacion.setFechaPlantado(fechaPlantado);
        plantacion.setFechaFinalizacion(parseOptionalDate(request.fechaFinalizacion));
        plantacion.setEstado(isBlank(request.estado) ? "ACTIVA" : request.estado.toUpperCase());
        return plantacion;
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.valueOf(value.trim());
        } catch (Exception ex) {
            return null;
        }
    }

    private LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value.trim().substring(0, 10));
        } catch (DateTimeParseException | IndexOutOfBoundsException ex) {
            return null;
        }
    }

    private LocalDate parseOptionalDate(String value) {
        if (isBlank(value)) {
            return null;
        }
        return parseDate(value);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}