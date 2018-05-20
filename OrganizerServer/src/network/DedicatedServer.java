package network;

import db.DataBaseManager;
import model.project.Project;
import model.ServerObjectType;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe que representa un servidor dedicad per cada client.
 */
public class DedicatedServer extends Thread{

    private boolean isOn;
    private Socket sClient;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private Server server;
    private String hash;
    private String username;
    private DedicatedServerProvidable provider;
    private HashMap<ServerObjectType, Communicable> communicators;

    public DedicatedServer(Socket sClient, Server server, DedicatedServerProvidable provider) {
        this.isOn = false;
        this.sClient = sClient;
        this.server = server;
        this.hash = null;
        this.provider = provider;
        communicators = CommunicatorsFactory.create();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Funcio que inicia el servidor dedicat
     */
    public void startDedicatedServer() {
        this.isOn = true;
        this.start();
    }

    /**
     * Funcio que apaga el servidor dedicat
     */
    public void stopDedicatedServer() {
        this.isOn = false;
        this.interrupt();
    }

    @Override
    public void run() {
        ServerObjectType type;

        try {
            objectOut = new ObjectOutputStream(sClient.getOutputStream());
            objectIn = new ObjectInputStream(sClient.getInputStream());

            //Mentres hi hagi comunicacio amb el client llegirem el tipus de dades que
            // ens volen enviar i segons aquest tipus obrirem un comunicator o un altre
            //que s'encarregara de controlar la resposta.
            while(isOn) {
                type = ServerObjectType.valueOf(objectIn.readInt());
                Communicable communicator = communicators.get(type);
                if (communicator != null) {
                    communicators.get(type).communicate(this, provider);
                }
            }
        } catch (IOException e) {
            //Si s'acaba la comunicacio amb el client eliminem i apaguem el dedicated server
            if (hash != null) {
                provider.deleteDedicated(hash,this);
            } else {
                provider.deleteFromLobby(this);
            }
            stopDedicatedServer();
        }
    }

    /**
     * Funcio encarregada de llegir dades del inputStream
     * @return Objecte llegit
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object readData() throws IOException, ClassNotFoundException {
        return objectIn.readObject();
    }

    /**
     * Funcio que envia al client la llista de projectes
     */
    public void sendProjectList() {
        ArrayList<Project> projectsOwner = DataBaseManager.getInstance().getProjectDBManager().getProjectsOwner(username);
        ArrayList<Project> projectsMember = DataBaseManager.getInstance().getProjectDBManager().getProjectsMember(username);
        sendData(null, projectsOwner.size());

        for (Project p : projectsOwner) {
            sendData(null, p);
        }
        sendData(null, projectsMember.size());
        for (Project p : projectsMember) {
            sendData(null, p);
        }
    }

    /**
     * Funcio que s'encarrega d'enviar dades al client
     * @param type Tipus de comunicacio
     * @param obj Objecte a enviar
     */
    public void sendData(ServerObjectType type, Object obj){
        try {
            objectOut.flush();
            if (type != null) {
                objectOut.writeInt(type.getValue());
            }
            objectOut.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}