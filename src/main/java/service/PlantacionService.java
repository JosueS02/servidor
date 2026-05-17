package service;

import java.util.List;
import java.util.Optional;
import model.Plantacion;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.PlantacionRepository;

@Service
public class PlantacionService {

    @Autowired
    private PlantacionRepository plantacionRepository;

    public List<Plantacion> listarPorUsuario(Integer idUsuario, String estado) {
        if (estado == null || estado.isBlank() || "TODAS".equalsIgnoreCase(estado)) {
            return plantacionRepository.findByInvernadero_Usuario_IdUsuario(idUsuario);
        }

        return plantacionRepository.findByInvernadero_Usuario_IdUsuarioAndEstado(idUsuario, estado.toUpperCase());
    }

    public Optional<Plantacion> obtenerPorId(Integer id) {
        return plantacionRepository.findById(id);
    }

    public Plantacion guardar(Plantacion plantacion) {
        return plantacionRepository.save(plantacion);
    }

    public void eliminar(Integer id) {
        plantacionRepository.deleteById(id);
    }

    public Optional<Plantacion> obtenerActivaPorInvernadero(Integer idInvernadero) {
        return plantacionRepository.findByInvernadero_IdInvernaderoAndEstado(idInvernadero, "ACTIVA");
    }

    public Optional<Plantacion> finalizarActivaPorInvernadero(Integer idUsuario, Integer idInvernadero) {
        Optional<Plantacion> activa = plantacionRepository
                .findFirstByInvernadero_IdInvernaderoAndInvernadero_Usuario_IdUsuarioAndEstado(idInvernadero, idUsuario, "ACTIVA");

        if (activa.isEmpty()) {
            return Optional.empty();
        }

        Plantacion plantacion = activa.get();
        plantacion.setEstado("FINALIZADA");
        plantacion.setFechaFinalizacion(LocalDate.now());
        return Optional.of(plantacionRepository.save(plantacion));
    }
}