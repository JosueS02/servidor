package repository;

import model.CatalogoSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josue
 */
@Repository
public interface CatalogoSensorRepository extends JpaRepository<CatalogoSensor, Integer> {
}
