package repository;

import java.util.List;
import model.InvernaderoActuador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josue
 */
@Repository
public interface InvernaderoActuadorRepository extends JpaRepository<InvernaderoActuador, Integer> {
    List<InvernaderoActuador>findByInvernadero_IdInvernadero(Integer idInvernadero);
}
