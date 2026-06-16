package service;

import config.MqttGateway;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import model.HistorialActuador;
import model.InvernaderoActuador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.HistorialActuadorRepository;
import repository.InvernaderoActuadorRepository;

/**
 *
 * @author josue
 */
@Service
public class ActuadorService {

    @Autowired
    private InvernaderoActuadorRepository actuadorRepository;

    @Autowired
    private HistorialActuadorRepository historialRepository;

    @Autowired
    private MqttGateway mqttGateway; // El puente que creamos arriba

    public List<InvernaderoActuador> listarPorInvernadero(Integer idInvernadero) {
        return actuadorRepository.findAll().stream()
                .filter(actuador -> actuador.getInvernadero() != null
                && actuador.getInvernadero().getIdInvernadero() != null
                && actuador.getInvernadero().getIdInvernadero().equals(idInvernadero))
                .collect(Collectors.toList());
    }

    public void cambiarEstado(Integer idInvActuador, Boolean nuevoEstado) {
        InvernaderoActuador actuador = actuadorRepository.findById(idInvActuador)
                .orElseThrow(() -> new RuntimeException("Actuador no encontrado"));
    
        actuador.setEstadoOperativo(nuevoEstado ? "ON" : "OFF");
        actuadorRepository.save(actuador);
        // 1. Guardar en el historial de la BD
        HistorialActuador registro = new HistorialActuador();
        registro.setInvernaderoActuador(actuador);
        registro.setEstado(nuevoEstado);
        registro.setFechaHora(LocalDateTime.now());
        historialRepository.save(registro);

        // 2. Enviar comando por MQTT a Mosquitto
        // Tópico sugerido: invernadero/actuadores/{id}
        String topic = "invernadero/actuadores/" + idInvActuador;
        String payload = nuevoEstado ? "ON" : "OFF";

        mqttGateway.sendToMqtt(payload, topic);

        System.out.println("🔌 [Comando] Enviado " + payload + " al tópico: " + topic);
    }
}
