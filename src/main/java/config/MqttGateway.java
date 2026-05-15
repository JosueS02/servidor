package config;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 *
 * @author josue
 */
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {

    // Este método nos permitirá enviar texto a cualquier tópico
    void sendToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic);
}
