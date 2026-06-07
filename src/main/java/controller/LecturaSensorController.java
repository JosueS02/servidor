package controller;

import java.util.List;
import model.LecturaSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.LecturaSensorService;

/**
 *
 * @author josue
 */
@RestController
@RequestMapping("/api/lecturas")
@CrossOrigin(origins = "*") // Permite que tu front lo consulte
public class LecturaSensorController {

    @Autowired
    private LecturaSensorService lecturaSensorService;

    // GET: http://localhost:8080/api/lecturas
    @GetMapping
    public ResponseEntity<List<LecturaSensor>> listarTodas() {
        return ResponseEntity.ok(lecturaSensorService.obtenerTodas());
    }

    // GET: http://localhost:8080/api/lecturas/sensor/1
    // Este endpoint se usa para graficar el historial de un sensor en específico.
    @GetMapping("/sensor/{idInvSensor}")
    public ResponseEntity<List<LecturaSensor>> obtenerPorSensor(@PathVariable Integer idInvSensor) {
        List<LecturaSensor> historial = lecturaSensorService.obtenerHistorialPorSensor(idInvSensor);
        if (historial.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(historial);
    }

    // POST: http://localhost:8080/api/lecturas
    // (Ahorita lo usamos para probar desde Postman, pero en el futuro, MQTT lo llamará automáticamente)
    @PostMapping
    public ResponseEntity<LecturaSensor> crearLectura(@RequestBody LecturaSensor lectura) {
        LecturaSensor nuevaLectura = lecturaSensorService.guardar(lectura);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaLectura);
    }
    
}
