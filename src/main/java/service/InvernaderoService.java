package service;

import java.util.List;
import java.util.Optional;
import model.Invernadero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.InvernaderoRepository;

/**
 *
 * @author josue
 */
@Service
public class InvernaderoService {

    @Autowired
    private InvernaderoRepository invernaderoRepository;

    public List<Invernadero> obtenerTodos() {
        return invernaderoRepository.findAll();
    }

    public Optional<Invernadero> obtenerPorId(Integer id) {
        return invernaderoRepository.findById(id);
    }

    public Invernadero guardar(Invernadero invernadero) {
        return invernaderoRepository.save(invernadero);
    }

    public void eliminar(Integer id) {
        invernaderoRepository.deleteById(id);
    }
}
