package repository;

import java.util.Optional;
import model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josue
 */
@Repository
public interface TopicoRepository extends JpaRepository<Topico, Integer> {

    // Para saber de qué sensor/actuador viene el mensaje cuando Mosquitto nos hable
    Optional<Topico> findByRuta(String ruta);
}
