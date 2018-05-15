package network;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.*;
import model.user.UserLogIn;
import model.user.UserRegister;
import network.communicators.RegisterCommunicator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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
        communicators = new HashMap<>();
        communicators.put(ServerObjectType.REGISTER, new RegisterCommunicator());
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

    public void startDedicatedServer() {
        this.isOn = true;
        this.start();
    }

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

            while(isOn) {
                type = ServerObjectType.valueOf(objectIn.readInt());
                Communicable communicator = communicators.get(type);
                if (communicator != null) {
                    communicators.get(type).communicate(this, provider);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readData() throws IOException, ClassNotFoundException {
        return objectIn.readObject();
    }

    public void sendProjectList() {
        ArrayList<Project> projectsOwner = DataBaseManager.getProjectDBManager().getProjectsOwner(username);
        ArrayList<Project> projectsMember = DataBaseManager.getProjectDBManager().getProjectsMember(username);
        sendData(ServerObjectType.GET_PROJECT_LIST, projectsOwner.size());
        for (Project p : projectsOwner) {
            sendData(null, p);
        }
        sendData(null, projectsMember.size());
        for (Project p : projectsMember) {
            sendData(null, p);
        }
    }

    public void sendData(ServerObjectType type, Object obj){
        try {
            objectOut.reset();
            if (type != null) {
                objectOut.writeInt(type.getValue());
            }
            objectOut.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}