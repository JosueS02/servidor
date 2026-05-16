package service;

import java.util.List;
import model.CatalogoSensor;
import model.Cultivo;
import model.Invernadero;
import model.InvernaderoActuador;
import model.InvernaderoSensor;
import model.LecturaSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.InvernaderoActuadorRepository;
import repository.PlantacionRepository;

/**
 *
 * @author josue
 */
@Service
public class RuleEngineService {

    @Autowired
    private PlantacionRepository plantacionRepository;

    @Autowired
    private InvernaderoActuadorRepository actuadorRepository;

    @Autowired
    private ActuadorService actuadorService;

    public void evaluarLectura(LecturaSensor lectura) {
        InvernaderoSensor invSensor = lectura.getInvernaderoSensor();
        Invernadero invernadero = invSensor.getInvernadero();
        CatalogoSensor tipoSensor = invSensor.getSensor();

        Float valorActual = lectura.getValor();
        String tipoMedicion = tipoSensor.getNombre().toLowerCase(); // ej: "temperatura", "humedad"

        // 1. Obtener el cultivo activo de este invernadero
        // Asumimos que creaste un método en PlantacionRepository como: 
        // findByInvernadero_IdInvernaderoAndEstado(id, "Activo")
        // Para simplificar, asumiremos que obtenemos el cultivo actual:
        Cultivo cultivo = obtenerCultivoActivo(invernadero.getIdInvernadero());
        if (cultivo == null) {
            return; // Si no hay nada plantado, no hay reglas que aplicar
        }
        // 2. Buscar los actuadores de este invernadero para poder controlarlos
        List<InvernaderoActuador> actuadores = actuadorRepository.findAll();
        // Nota: Deberías filtrar solo los de este invernadero en producción

        // 3. EVALUACIÓN DE REGLAS SEGÚN EL TIPO DE SENSOR
        if (tipoMedicion.contains("temperatura")) {
            evaluarTemperatura(valorActual, cultivo, actuadores);
        } else if (tipoMedicion.contains("humedad")) {
            evaluarHumedad(valorActual, cultivo, actuadores);
        }
        // Puedes agregar más else if para CO2 y Luz
    }

    private void evaluarTemperatura(Float tempActual, Cultivo cultivo, List<InvernaderoActuador> actuadores) {
        // Buscamos un actuador tipo "Ventilador" en la lista
        InvernaderoActuador ventilador = actuadores.stream()
                .filter(a -> a.getActuador().getNombre().toLowerCase().contains("ventilador"))
                .findFirst().orElse(null);

        if (ventilador != null) {
            // REGLA: Si hace mucho calor, encender ventilador
            if (tempActual > cultivo.getTemperaturaMax()) {
                if (!"ON".equals(ventilador.getEstadoOperativo())) {
                    actuadorService.cambiarEstado(ventilador.getIdInvActuador(), true);
                    ventilador.setEstadoOperativo("ON");
                }
            } // REGLA: Si la temperatura ya es normal o baja, apagar ventilador
            else if (tempActual <= cultivo.getTemperaturaMax() && "ON".equals(ventilador.getEstadoOperativo())) {
                actuadorService.cambiarEstado(ventilador.getIdInvActuador(), false);
                ventilador.setEstadoOperativo("OFF");
            }
        }
    }

    private void evaluarHumedad(Float humActual, Cultivo cultivo, List<InvernaderoActuador> actuadores) {
        // Buscamos un actuador tipo "Riego" o "Bomba"
        InvernaderoActuador riego = actuadores.stream()
                .filter(a -> a.getActuador().getNombre().toLowerCase().contains("riego"))
                .findFirst().orElse(null);

        if (riego != null) {
            // REGLA: Si está muy seco, encender el riego
            if (humActual < cultivo.getHumedadMin()) {
                if (!"ON".equals(riego.getEstadoOperativo())) {
                    actuadorService.cambiarEstado(riego.getIdInvActuador(), true);
                    riego.setEstadoOperativo("ON");
                }
            } // REGLA: Si ya llegamos a la humedad máxima ideal, apagar el riego
            else if (humActual >= cultivo.getHumedadMax() && "ON".equals(riego.getEstadoOperativo())) {
                actuadorService.cambiarEstado(riego.getIdInvActuador(), false);
                riego.setEstadoOperativo("OFF");
            }
        }
    }

    // Método de apoyo para obtener el cultivo (Aquí implementarías tu consulta real a Plantacion)
    private Cultivo obtenerCultivoActivo(Integer idInvernadero) {
        // Lógica para ir a la tabla Plantacion, buscar por idInvernadero donde estado = 'Activo'
        // y retornar su Cultivo.
        return null; // Cambiar por la consulta real
    }
}
