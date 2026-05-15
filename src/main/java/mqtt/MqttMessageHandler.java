package mqtt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.InvernaderoSensor;
import model.LecturaSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import repository.InvernaderoSensorRepository;
import service.LecturaSensorService;
import service.RuleEngineService;

/**
 *
 * @author josue
 */
@Component
public class MqttMessageHandler {

    @Autowired
    private LecturaSensorService lecturaSensorService;

    @Autowired
    private InvernaderoSensorRepository invSensorRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; 
    
    @Autowired
    private RuleEngineService ruleEngine;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void procesarMensaje(Message<?> message) {
        try {
            String payload = message.getPayload().toString();
            JsonNode jsonNode = objectMapper.readTree(payload);

            Integer idInvSensor = jsonNode.get("idInvSensor").asInt();
            Float valor = (float) jsonNode.get("valor").asDouble();

            InvernaderoSensor sensor = invSensorRepository.findById(idInvSensor).orElse(null);

            if (sensor != null) {
                // 1. Persistencia en Base de Datos
                LecturaSensor nuevaLectura = new LecturaSensor();
                nuevaLectura.setInvernaderoSensor(sensor);
                nuevaLectura.setValor(valor);
                lecturaSensorService.guardar(nuevaLectura);

                // 2. Tiempo Real (WebSocket) para el FrontEnd
                String wsTopic = "/topic/sensor/" + idInvSensor;
                messagingTemplate.convertAndSend(wsTopic, nuevaLectura);
                System.out.println("🚀 [WS] Dato enviado a " + wsTopic);

                // 3. MOTOR DE REGLAS (Aquí evaluamos si hay que encender o apagar algo)
                ruleEngine.evaluarLectura(nuevaLectura);

            } else {
                System.err.println("⚠️ Error: No existe el InvernaderoSensor con ID " + idInvSensor);
            }
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}