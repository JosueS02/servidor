package repository;

import java.util.List;
import model.LecturaSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josue
 */
@Repository
public interface LecturaSensorRepository extends JpaRepository<LecturaSensor, Long> {

    // Nos permite obtener todo el historial de un sensor específico ordenado por el más reciente
    List<LecturaSensor> findByInvernaderoSensor_IdInvSensorOrderByFechaHoraDesc(Integer idInvSensor);
}
