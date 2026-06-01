package config;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 *
 * @author josue
 */
@MessagingGateway(defaultRequestChannel =
        "mqttOutboundChannel")
public interface MqttGateway {

void sendToMqtt(
            @Header(MqttHeaders.TOPIC)
            String topic,

            String payload
    );

    void enviarComando(
            @Header(MqttHeaders.TOPIC)
            String topic,

            String payload
    );}
