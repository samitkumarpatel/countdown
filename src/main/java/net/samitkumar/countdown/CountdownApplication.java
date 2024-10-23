package net.samitkumar.countdown;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.stream.IntStream;

@SpringBootApplication
public class CountdownApplication {

	public static void main(String[] args) {
		SpringApplication.run(CountdownApplication.class, args);
	}
}

@Component
@Slf4j
class CountdownHandler extends TextWebSocketHandler {

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		int count = Integer.parseInt(message.getPayload());
		IntStream.rangeClosed(1, count)
				.forEach(i -> {
					try {
						session.sendMessage(new TextMessage(String.valueOf(i)));
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
	}
}

@Configuration
@EnableWebSocket
class WebSocketConfig implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(countDownHandler(), "/ws/countdown").setAllowedOriginPatterns("*");
	}

	@Bean
	public WebSocketHandler countDownHandler() {
		return new CountdownHandler();
	}
}