package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 *
 * @author josue
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilita un broker simple en memoria para enviar mensajes al front
        // El front se suscribirá a rutas que empiecen con /topic
        config.enableSimpleBroker("/topic");

        // Prefijo para los mensajes que el front envíe al servidor (opcional)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // El punto de conexión para el Frontend
        registry.addEndpoint("/ws-invernadero")
                .setAllowedOrigins("*") // En producción, pon aquí la URL de tu front
                .withSockJS(); // Soporte para navegadores viejos
    }
}
