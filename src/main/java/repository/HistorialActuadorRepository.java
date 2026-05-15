package repository;

import model.HistorialActuador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josue
 */
@Repository
public interface HistorialActuadorRepository extends JpaRepository<HistorialActuador, Long> {
}
