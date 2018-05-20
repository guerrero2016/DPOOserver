package network;

import utils.Configuration;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Classe que hereda de Thread que s'encarrega d'establir connexió amb els clients.
 */
public class Server extends Thread{
    private static final int SERVER_PORT = Configuration.getInstance().getCommunicationPort();

    private boolean isOn;
    private ServerSocket serverSocket;
    private LinkedList<DedicatedServer> clients;
    private DedicatedServerProvidable projectServers;

    /**
     * Crea el servidor amb totes les inicialitzacions que fan falta per al seu funcionament.
     */
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

    /**
     * Procediment que inicia el servidor
     */
    public void startServer() {
        this.isOn = true;
        this.start();
    }

    /**
     * Procediment que atura el servidor
     */
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
