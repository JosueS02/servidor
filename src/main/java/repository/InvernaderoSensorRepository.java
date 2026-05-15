package repository;

import model.InvernaderoSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josue
 */
@Repository
public interface InvernaderoSensorRepository extends JpaRepository<InvernaderoSensor, Integer> {
}
