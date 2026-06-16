package controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.Cultivo;
import model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CultivoService;
import service.UsuarioService;

/**
 *
 * @author josue
 */
@RestController
@CrossOrigin(origins = "*")
public class CultivoController {

    @Autowired
    private CultivoService cultivoService;
    @Autowired
    private UsuarioService usuarioService;

    // Clase interna DTO para asegurar compatibilidad total de nombres de variables con el Frontend
    public static class CultivoDTO {
        public String id; 
        public String idCultivo; 
        public String idUsuario;
        public UsuarioDTO usuario;
        public String nombre;
        public Float temperaturaMin;
        public Float temperaturaMax;
        public Float humedadMin;
        public Float humedadMax;
        public Float luzMin;
        public Float luzMax;
        public Float co2Max; // <-- 1. SE AGREGÓ ESTA LÍNEA

        public CultivoDTO(Cultivo c) {
            this.id = String.valueOf(c.getIdCultivo());
            this.idCultivo = String.valueOf(c.getIdCultivo());
            this.idUsuario = c.getUsuario() != null ? String.valueOf(c.getUsuario().getIdUsuario()) : null;
            this.usuario = c.getUsuario() != null ? new UsuarioDTO(c.getUsuario()) : null;
            this.nombre = c.getNombre();
            this.temperaturaMin = c.getTemperaturaMin();
            this.temperaturaMax = c.getTemperaturaMax();
            this.humedadMin = c.getHumedadMin();
            this.humedadMax = c.getHumedadMax();
            this.luzMin = c.getLuzMin();
            this.luzMax = c.getLuzMax();
            this.co2Max = c.getCo2Max(); // <-- 2. SE AGREGÓ ESTA LÍNEA PARA ENVIARLO AL FRONTEND
        }
    }

    public static class UsuarioDTO {
        public String idUsuario;
        public String correo;

        public UsuarioDTO(Usuario usuario) {
            this.idUsuario = usuario != null ? String.valueOf(usuario.getIdUsuario()) : null;
            this.correo = usuario != null ? usuario.getCorreo() : null;
        }
    }

    // GET: Soporta tanto la administración común como el selector de la simulación
    @GetMapping({"/api/cultivos", "/api/crops", "/api/simulation/crops"})
    public ResponseEntity<List<CultivoDTO>> listar(@RequestParam(value = "userId", required = false) Integer userId) {
        List<CultivoDTO> dtos = cultivoService.obtenerTodos().stream()
                .filter(c -> userId == null || (c.getUsuario() != null && userId.equals(c.getUsuario().getIdUsuario())))
                .map(CultivoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET: Obtener detalle por ID
    @GetMapping({"/api/cultivos/{id}", "/api/crops/{id}"})
    public ResponseEntity<CultivoDTO> buscarPorId(@PathVariable Integer id) {
        return cultivoService.obtenerPorId(id)
                .map(c -> ResponseEntity.ok(new CultivoDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Registrar un nuevo tipo de cultivo
    @PostMapping({"/api/cultivos", "/api/crops"})
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> payload) {
        String nombre = (String) payload.get("nombre");
        
        // Validación robusta de idUsuario ya sea plano o anidado
        Object idUsuarioObj = payload.get("idUsuario");
        if (idUsuarioObj == null && payload.containsKey("usuario")) {
            Map<?, ?> usrMap = (Map<?, ?>) payload.get("usuario");
            idUsuarioObj = usrMap.get("idUsuario");
        }

        if (idUsuarioObj == null) {
            return ResponseEntity.badRequest().body("El usuario es requerido para crear el cultivo.");
        }

        Integer idUsuario = Integer.parseInt(idUsuarioObj.toString());
        Usuario usuarioExistente = usuarioService.obtenerPorId(idUsuario).orElse(null);
        if (usuarioExistente == null) {
            return ResponseEntity.badRequest().body("El usuario indicado no existe. Verifica la sesion iniciada.");
        }

        // Mapeo seguro de los valores numéricos provenientes del JSON
        Cultivo cultivo = new Cultivo();
        cultivo.setNombre(nombre);
        cultivo.setUsuario(usuarioExistente);
        cultivo.setTemperaturaMin(payload.get("temperaturaMin") != null ? Float.parseFloat(payload.get("temperaturaMin").toString()) : null);
        cultivo.setTemperaturaMax(payload.get("temperaturaMax") != null ? Float.parseFloat(payload.get("temperaturaMax").toString()) : null);
        cultivo.setHumedadMin(payload.get("humedadMin") != null ? Float.parseFloat(payload.get("humedadMin").toString()) : null);
        cultivo.setHumedadMax(payload.get("humedadMax") != null ? Float.parseFloat(payload.get("humedadMax").toString()) : null);
        cultivo.setLuzMin(payload.get("luzMin") != null ? Float.parseFloat(payload.get("luzMin").toString()) : null);
        cultivo.setLuzMax(payload.get("luzMax") != null ? Float.parseFloat(payload.get("luzMax").toString()) : null);
        
        // <-- 3. SE AGREGÓ ESTA LÍNEA PARA LEER EL CO2 DEL JSON Y GUARDARLO EN LA BD
        cultivo.setCo2Max(payload.get("co2Max") != null ? Float.parseFloat(payload.get("co2Max").toString()) : null); 

        Cultivo nuevo = cultivoService.guardar(cultivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CultivoDTO(nuevo));
    }

    // PUT: Actualizar parámetros de un cultivo existente
    @PutMapping({"/api/cultivos/{id}", "/api/crops/{id}"})
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Cultivo cultivo) {
        if (!cultivoService.obtenerPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        cultivo.setIdCultivo(id); 
        Cultivo actualizado = cultivoService.guardar(cultivo);
        return ResponseEntity.ok(new CultivoDTO(actualizado));
    }

    // DELETE: Eliminar un cultivo
    @DeleteMapping({"/api/cultivos/{id}", "/api/crops/{id}"})
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        cultivoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}