/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

/**
 *
 * @author pedro
 */
@Service
public class MqttCommandPublisher {

    private final MqttClient client;

    public MqttCommandPublisher() throws Exception {

        client = new MqttClient(
                "tcp://localhost:1883",
                "backend-publisher"
        );

        MqttConnectOptions options =
                new MqttConnectOptions();

        options.setAutomaticReconnect(true);
        options.setCleanSession(true);

        client.connect(options);
    }

    public void publicarComando(
            Integer idInvernadero,
            String actuador,
            String accion
    ) throws Exception {

        String topic =
                "invernadero/"
                + idInvernadero
                + "/comandos";

        String payload =
                "{\"actuador\":\""
                + actuador
                + "\",\"accion\":\""
                + accion
                + "\"}";

        MqttMessage message =
                new MqttMessage(
                        payload.getBytes()
                );

        client.publish(topic, message);
    }
}
