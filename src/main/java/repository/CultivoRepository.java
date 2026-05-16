package repository;

import model.Cultivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josue
 */
@Repository
public interface CultivoRepository extends JpaRepository<Cultivo, Integer> {
}
