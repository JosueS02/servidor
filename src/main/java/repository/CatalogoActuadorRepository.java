package repository;

import model.CatalogoActuador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josue
 */
@Repository
public interface CatalogoActuadorRepository extends JpaRepository<CatalogoActuador, Integer> {
}
