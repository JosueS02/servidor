package repository;

import java.util.List;
import java.util.Optional;
import model.Plantacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josue
 */
@Repository
public interface PlantacionRepository extends JpaRepository<Plantacion, Integer> {

	List<Plantacion> findByInvernadero_Usuario_IdUsuario(Integer idUsuario);

	List<Plantacion> findByInvernadero_Usuario_IdUsuarioAndEstado(Integer idUsuario, String estado);

	Optional<Plantacion> findByInvernadero_IdInvernaderoAndEstado(Integer idInvernadero, String estado);

	Optional<Plantacion> findByInvernadero_IdInvernadero(Integer idInvernadero);

	Optional<Plantacion> findFirstByInvernadero_IdInvernaderoAndInvernadero_Usuario_IdUsuarioAndEstado(
			Integer idInvernadero,
			Integer idUsuario,
			String estado);
     
}
