package dev.backend.interview.server;

import lombok.extern.log4j.Log4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;

@Log4j
public class WebSocketHandler1 extends AbstractWebSocketHandler {
    public static String end = "\n";
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Session: " + session);
        session.sendMessage(new TextMessage("HI, I'M  " + session.getId() + end));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println("Session: " + session +" Message: " + message);
        session.sendMessage(new TextMessage("HI, I'M  " + session.getId()+ end));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Session: " + session + " status: " + status);
        super.afterConnectionClosed(session, status);
    }
}