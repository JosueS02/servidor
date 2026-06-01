package repository;

import java.util.List;
import java.util.Optional;
import model.LecturaSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josue
 */
@Repository
public interface LecturaSensorRepository extends JpaRepository<LecturaSensor, Long> {

//    Optional<LecturaSensor>
//    findTopByInvernaderoSensor_IdInvSensorOrderByFechaHoraDesc(Integer idInvSensor);
//            
    Optional<LecturaSensor>
    findTopByInvernaderoSensor_IdInvSensorOrderByIdLecturaDesc(Integer idInvSensor);

    List<LecturaSensor>
    findByInvernaderoSensor_IdInvSensorOrderByIdLecturaAsc(Integer idInvSensor);

}
