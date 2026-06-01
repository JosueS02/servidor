package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

public Optional<LecturaSensor>
obtenerUltimaLectura(
        Integer idInvSensor
) {

    return lecturaSensorRepository.findTopByInvernaderoSensor_IdInvSensorOrderByIdLecturaDesc(idInvSensor);                                
}

    public List<LecturaSensor>
    obtenerHistorialPorSensor(
            Integer idInvSensor
    ) {

        return lecturaSensorRepository
                .findByInvernaderoSensor_IdInvSensorOrderByIdLecturaAsc(
                        idInvSensor
                );
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
