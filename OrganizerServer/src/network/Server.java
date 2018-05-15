package network;

import utils.Configuration;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server extends Thread{
    private static final int SERVER_PORT = Configuration.getServerPort();

    private boolean isOn;
    private ServerSocket serverSocket;
    private LinkedList<DedicatedServer> clients;
    private DedicatedServerProvidable projectServers;

    public Server() {
        try {
            //model i vista si fos necessari
            this.isOn = false;
            this.serverSocket = new ServerSocket(SERVER_PORT);
            this.clients = new LinkedList<>();
            this.projectServers = new DedicatedServerProvider();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        this.isOn = true;
        this.start();
    }

    public void stopServer() {
        this.isOn = false;
        this.interrupt();
    }

    @Override
    public void run() {
        while (isOn) {
            try {
                Socket sClient = serverSocket.accept();
                DedicatedServer dsClient = new DedicatedServer(sClient, this, projectServers);
                clients.add(dsClient);
                dsClient.startDedicatedServer();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        for (DedicatedServer ds : clients) {
            ds.stopDedicatedServer();
        }
    }
}
