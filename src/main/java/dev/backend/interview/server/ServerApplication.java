package dev.backend.interview.server;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
        try {
            EchoMultiServer server = new EchoMultiServer();
            server.start(50000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
