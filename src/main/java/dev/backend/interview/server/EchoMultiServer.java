package dev.backend.interview.server;

import dev.backend.interview.server.dev.backend.interview.server.command.Command;
import dev.backend.interview.server.dev.backend.interview.server.command.CommandDispatcher;
import dev.backend.interview.server.dev.backend.interview.server.model.Model;
import dev.backend.interview.server.dev.backend.interview.server.model.ModelImpl;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

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

        Model model = new ModelImpl();
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
        private CommandDispatcher dispatcher;

        public EchoClientHandler(Socket socket, Model model) {
            this.dispatcher = new CommandDispatcher();
            this.clientSocket = socket;
            this.context = new SessionContext();
            this.context.setId(UUID.randomUUID());
            this.context.setStartTime(new Date().getTime());
            this.context.setModel(model);
            try {
                this.clientSocket.setSoTimeout(30000);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            log("Server", clientSocket.getPort(), "New connection accepted");
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                Command connectCommand = dispatcher.getConnectCommand();
                final String message = connectCommand.execute(context, null);
                log("Server", clientSocket.getPort(), message);
                out.println(message);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    log("Client", clientSocket.getPort(), inputLine);
                    Command command = dispatcher.getCommand(inputLine);
                    final String response = command.execute(context, inputLine);
                    log("Server", clientSocket.getPort(), response);
                    out.println(response);
                }
            } catch (SocketTimeoutException so){
                Command byeCommand = dispatcher.getByeCommand();
                final String response = byeCommand.execute(context, null);
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

        private void log(String who, int port, String message){
            System.out.println(who + " on socket: " + port + " message: " + message);
        }

    }

    public static void main(String[] args) {
        EchoMultiServer server = new EchoMultiServer();
        try {
            server.start(50000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}