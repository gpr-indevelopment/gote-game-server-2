package com.gpr.edgegameserver.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketTextHandler webSocketTextHandler;

    public WebSocketConfig(WebSocketTextHandler webSocketTextHandler) {
        this.webSocketTextHandler = webSocketTextHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketTextHandler, "/server");
    }
}
