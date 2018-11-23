package dev.backend.interview.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class GreetServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        System.out.println("New connection accepted " +
                clientSocket.getInetAddress() +
                ":" + clientSocket.getPort());
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        final UUID session = UUID.randomUUID();
        final String message = "HI, I’M " + session + "\n";
        System.out.println(message);
        out.println(message);

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if ("BYE MATE!".equals(inputLine)) {
                out.println("BYE <name>, WE SPOKE FOR <X> MS");
                break;
            }
            out.println(inputLine);
        }
    }

    public void stop() throws IOException{
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
    public static void main(String[] args) {
        GreetServer server=new GreetServer();
        try {
            server.start(50000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}