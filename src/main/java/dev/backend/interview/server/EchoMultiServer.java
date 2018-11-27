package dev.backend.interview.server;

import dev.backend.interview.server.command.Command;
import dev.backend.interview.server.command.CommandController;
import dev.backend.interview.server.model.Model;
import dev.backend.interview.server.model.ModelImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.UUID;

public class EchoMultiServer {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        Model model = null; //new ModelImpl();
        serverSocket = new ServerSocket(port);

        while (true)
            new EchoClientHandler(serverSocket.accept(), model).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private SessionContext context;
        private CommandController controller;

        public EchoClientHandler(Socket socket, Model model) {
            this.controller = new CommandController();
            this.clientSocket = socket;
            try {
                this.clientSocket.setSoTimeout(30000);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            initContext(model);
            log("Server", clientSocket.getPort(), "New connection accepted");
        }

        private void initContext(Model model) {
            this.context = new SessionContext();
            this.context.setId(UUID.randomUUID());
            this.context.setStartTime(new Date().getTime());
            this.context.setModel(model);
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                final String message = controller.execute(Command.CONNECT_NAME, context);
                log("Server", clientSocket.getPort(), message);

                out.println(message);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    log("Client", clientSocket.getPort(), inputLine);

                    final String response = controller.execute(inputLine, context);
                    log("Server", clientSocket.getPort(), response);

                    out.println(response);
                }
            } catch (SocketTimeoutException so) {
                final String response = controller.execute(Command.BYE_MATE_NAME, context);
                log("Server", clientSocket.getPort(), response);

                out.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void log(String who, int port, String message) {
            String text = who + " on socket: " + port + " message: " + message;
            System.out.println(text);
        }
    }
}