package dev.backend.interview.server;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private static final Logger logger = Logger.getLogger(Server.class);
    private final int serverPort;
    private final int socketTimeout;
    private ServerSocket serverSocket;
    private boolean isStopped;

    public Server(int port, int socketTimeout) {
        this.serverPort = port;
        this.socketTimeout = socketTimeout;
    }

    @Override
    public void run() {
        createServerSocket();
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        logger.info(String.format("Server on port: %d started", this.serverPort));
        while (!isStopped()) {
            Worker worker;
            try {
                final Socket clientSocket = this.serverSocket.accept();
                try {
                    clientSocket.setSoTimeout(socketTimeout);
                } catch (SocketException e) {
                    final String errorMessage = String.format("Error setting timeout: %d on port: %d",
                            socketTimeout, clientSocket.getPort());
                    logger.error(errorMessage, e);
                    throw new RuntimeException(errorMessage, e);
                }
                worker = new Worker(clientSocket);
            } catch (IOException e) {
                if (isStopped()) {
                    logger.info(String.format("Server on port: %d stopped", this.serverPort));
                    break;
                }
                final String errorMessage = String.format("Error accepting client connection on port: %d",
                        this.serverPort);
                logger.error(errorMessage, e);
                throw new RuntimeException(errorMessage, e);
            }
            threadPool.execute(worker);
        }
        threadPool.shutdown();
        logger.info(String.format("Server on port: %d stopped", this.serverPort));
    }

    private void stop() {
        logger.info(String.format("Server on port: %d stopping", this.serverPort));
        this.isStopped = true;
        try {
            if(serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            final String errorMessage = String.format("Error on closing server on port: %d", this.serverPort);
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    private void createServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            final String errorMessage = String.format("Error creating socket server on port: %d", this.serverPort);
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private static Properties loadProperties() {
        Properties serverProperties = new Properties();
        try {
            serverProperties.load(Server.class.getResourceAsStream("/server.properties"));
        } catch (IOException e) {
            logger.warn("Cannot load properties", e);
        }
        return serverProperties;
    }
    public static void main(String[] args) {
        if (args.length > 0 && args[args.length - 1].equals("debug")) {
            Logger.getRootLogger().setLevel(Level.DEBUG);
        }
        final Properties properties = loadProperties();
        final Integer serverPort = Integer.valueOf(properties.getProperty("server.socket.port", "50000"));
        final Integer socketTimeout = Integer.valueOf(properties.getProperty("server.socket.timeout", "30000"));

        Server server = new Server(serverPort, socketTimeout);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        new Thread(server).start();
    }
}