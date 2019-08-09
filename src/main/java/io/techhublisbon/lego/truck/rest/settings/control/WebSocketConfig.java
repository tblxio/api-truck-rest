package io.techhublisbon.lego.truck.rest.settings.control;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Getter
@Configuration
@EnableWebSocketMessageBroker
@EnableAsync
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * Prefix used for WebSocket destination mappings
     */
    private String applicationPrefix = "/app";
    /**
     * Prefix used by topics
     */
    private String topicPrefix = "/events/";
    /**
     * Endpoint that can be used to connect to
     */
    private String endpoint = "/messages";
    /**
     * Allowed origins
     */
    private String[] allowedOrigins = new String[0];

    @Override
    public void registerStompEndpoints(StompEndpointRegistry
                                               registry) {
        registry.addEndpoint(endpoint)
        ;
        registry.addEndpoint(endpoint)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(topicPrefix);
        config.setApplicationDestinationPrefixes(applicationPrefix);
    }
}