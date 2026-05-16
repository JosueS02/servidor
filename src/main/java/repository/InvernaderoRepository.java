package repository;

import model.Invernadero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josue
 */
@Repository
public interface InvernaderoRepository extends JpaRepository<Invernadero, Integer> {
}
