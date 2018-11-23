package dev.backend.interview.server;

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
        serverSocket = new ServerSocket(port);
        while (true)
            new EchoClientHandler(serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private UUID session;
        private long startTime;
        private String name;
        final String hiText = "HI, I'M ";

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.session = UUID.randomUUID();
            this.startTime = new Date().getTime();
            try {
                this.clientSocket.setSoTimeout(30000);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            System.out.println("New connection accepted " +
                    clientSocket.getInetAddress() +
                    ":" + clientSocket.getPort());
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                final String message = hiText + session;
                System.out.println("Server on socket:  " + clientSocket.getPort() + " message: " + message);
                out.println(message);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Client on socket: " + clientSocket.getPort() + " message: " + inputLine);
                    if (inputLine.startsWith(hiText)) {
                        this.name = inputLine.substring(inputLine.indexOf(hiText) + hiText.length());
                        final String response = "HI " + this.name;
                        System.out.println("Server on socket: " + clientSocket.getPort() + " message: " + response);
                        out.println(response);
                    } else if ("BYE MATE!".equals(inputLine)) {
                        final String response = "BYE " + this.name + ", WE SPOKE FOR " + (new Date().getTime() - this.startTime) + " MS";
                        System.out.println("Server on socket: " + clientSocket.getPort() + " message: " + response);
                        out.println(response);
                    } else {
                        final String response = "SORRY, I DIDN'T UNDERSTAND THAT";
                        System.out.println("Server on socket: " + clientSocket.getPort() + " message: " + response);
                        out.println(response);
                    }
                }
            } catch (SocketTimeoutException so){
                final String response = "BYE " + this.name + ", WE SPOKE FOR " + (new Date().getTime() - this.startTime) + " MS";
                System.out.println("Server on socket:  " + clientSocket.getPort() + " message: " + response);
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