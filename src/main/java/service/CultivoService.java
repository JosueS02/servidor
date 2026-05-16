package service;

import java.util.List;
import java.util.Optional;
import model.Cultivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CultivoRepository;

/**
 *
 * @author josue
 */
@Service
public class CultivoService {

    @Autowired
    private CultivoRepository cultivoRepository;

    public List<Cultivo> obtenerTodos() {
        return cultivoRepository.findAll();
    }

    public Optional<Cultivo> obtenerPorId(Integer id) {
        return cultivoRepository.findById(id);
    }

    public Cultivo guardar(Cultivo cultivo) {
        return cultivoRepository.save(cultivo);
    }

    public void eliminar(Integer id) {
        cultivoRepository.deleteById(id);
    }
}
