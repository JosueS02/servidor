package repository;

import model.Plantacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josue
 */
@Repository
public interface PlantacionRepository extends JpaRepository<Plantacion, Integer> {
}
