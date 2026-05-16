package config;

/**
 *
 * @author josue
 */
public interface MqttGateway {

    void sendToMqtt(String data, String topic);
}
