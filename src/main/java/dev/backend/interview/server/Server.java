package dev.backend.interview.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public class Server implements Runnable {
    private static final Logger logger = Logger.getLogger(Server.class);
    private final int serverPort;
    private ServerSocket serverSocket;
    private boolean isStopped;

    public Server(int port) {
        this.serverPort = port;
    }

    @Override
    public void run() {
        createServerSocket();
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        while (!isStopped()) {
            Worker worker;
            try {
                worker = new Worker(this.serverSocket.accept());
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

    public static void main(String[] args) {
        Server server = new Server(50000);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        new Thread(server).start();
    }
}