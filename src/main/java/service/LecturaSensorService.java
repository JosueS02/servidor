package service;

import java.time.LocalDateTime;
import java.util.List;
import model.LecturaSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.LecturaSensorRepository;

/**
 *
 * @author josue
 */
@Service
public class LecturaSensorService {

    @Autowired
    private LecturaSensorRepository lecturaSensorRepository;

    // Obtener todo el historial general (Cuidado si hay millones de datos, pero útil para probar)
    public List<LecturaSensor> obtenerTodas() {
        return lecturaSensorRepository.findAll();
    }

    // EL MÁS IMPORTANTE: Obtener el historial de un sensor específico (ej. "Dame la temperatura del Invernadero 1")
    public List<LecturaSensor> obtenerHistorialPorSensor(Integer idInvSensor) {
        return lecturaSensorRepository.findByInvernaderoSensor_IdInvSensorOrderByFechaHoraDesc(idInvSensor);
    }

    // Guardar una nueva lectura
    public LecturaSensor guardar(LecturaSensor lectura) {
        // Si la base de datos no le pone la fecha automáticamente, se la ponemos aquí
        if (lectura.getFechaHora() == null) {
            lectura.setFechaHora(LocalDateTime.now());
        }
        return lecturaSensorRepository.save(lectura);
    }
}
