package repository;

import java.util.Optional;
import model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josue
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Spring Boot crea automáticamente la consulta SQL para buscar por correo (ideal para el Login)
    Optional<Usuario> findByCorreo(String correo);
}
