package fusionIQ.AI.V2.fusionIq.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private TrainingWebSocketHandler webSocketHandler;

    @Autowired
    private NotificationWebSocketHandler notificationWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/training").setAllowedOrigins("*");
        registry.addHandler(notificationWebSocketHandler, "/notifications").setAllowedOrigins("*");
        addInterceptors(new HttpSessionHandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(
                    ServerHttpRequest request,
                    ServerHttpResponse response,
                    WebSocketHandler wsHandler,
                    Map<String, Object> attributes) throws Exception {
                // Extract userId from the request and add it to the session attributes
                String userId = request.getURI().getQuery().split("=")[1];
                attributes.put("userId", Long.parseLong(userId));
                return super.beforeHandshake(request, response, wsHandler, attributes);
            }
        });
    }

    private void addInterceptors(HttpSessionHandshakeInterceptor userId) {
    }
}
