package dev.backend.interview.server;

import dev.backend.interview.server.command.CommandBase;
import dev.backend.interview.server.command.CommandController;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.UUID;

class Worker implements Runnable {
    private static final Logger logger = Logger.getLogger(Worker.class);
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private SessionContext context;
    private CommandController controller;


    Worker(Socket socket) {
        initContext();
        this.controller = new CommandController();
        this.clientSocket = socket;
        try {
            this.clientSocket.setSoTimeout(30000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        logCommand("Server", clientSocket.getPort(), "New connection accepted");
    }

    private void initContext() {
        this.context = new SessionContext();
        this.context.setSessionId(UUID.randomUUID().toString());
        this.context.setStartTime(new Date().getTime());
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            final String message = controller.execute(CommandBase.CONNECT_NAME, context);
            logCommand("Server", clientSocket.getPort(), message);
            out.println(message);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                logCommand("Client", clientSocket.getPort(), inputLine);

                final String response = controller.execute(inputLine, context);
                logCommand("Server", clientSocket.getPort(), response);
                out.println(response);
            }
        } catch (SocketTimeoutException so) {
            final String response = controller.execute(CommandBase.BYE_MATE_NAME, context);
            logCommand("Server", clientSocket.getPort(), response);
            out.println(response);

        } catch (IOException e) {
            final String errorMessage = String.format("Error on communicate with client socket on port: %d",
                    clientSocket.getPort());
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);

        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                final String errorMessage = String.format("Error on closing client socket on port: %d",
                        clientSocket.getPort());
                logger.error(errorMessage, e);
            }
        }
    }

    private void logCommand(String who, int port, String message) {
        String text = who + " on socket: " + port + " message: " + message;
        logger.debug(text);
    }
}
