package controller;

import java.util.List;
import java.util.stream.Collectors;
import model.InvernaderoSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.InvernaderoSensorRepository;

/**
 *
 * @author josue
 */
@RestController
@RequestMapping("/api/sensores")
@CrossOrigin(origins = "*")
public class SensorController {

    @Autowired
    private InvernaderoSensorRepository invernaderoSensorRepository;

    // Listar sensores de un invernadero
    @GetMapping("/invernadero/{id}")
    public List<InvernaderoSensor> listar(@PathVariable Integer id) {
        return invernaderoSensorRepository.findAll().stream()
                .filter(sensor -> sensor.getInvernadero() != null
                && sensor.getInvernadero().getIdInvernadero() != null
                && sensor.getInvernadero().getIdInvernadero().equals(id))
                .collect(Collectors.toList());
    }
}