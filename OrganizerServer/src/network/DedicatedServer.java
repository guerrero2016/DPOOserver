package network;

import model.project.Project;
import model.user.LogIn;
import model.user.Register;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

public class DedicatedServer extends Thread{

    private boolean isOn;
    private Socket sClient;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private Server server;
    private LinkedList<DedicatedServer> clients;
    private String hash;
    private HashMap<String, LinkedList<DedicatedServer>> projectServers;


    public DedicatedServer(Socket sClient, Server server, LinkedList<DedicatedServer> clients,
                           HashMap<String, LinkedList<DedicatedServer>> projectServers) {
        this.isOn = false;
        this.sClient = sClient;
        this.server = server;
        this.clients = clients;
        this.hash = null;
        this.projectServers = projectServers;
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
        boolean logged = false;
        Object aux;

        try {
            objectIn = new ObjectInputStream(sClient.getInputStream());
            objectOut = new ObjectOutputStream(sClient.getOutputStream());

            while (!logged) {
                aux = objectIn.readObject();
                //TODO s'ha de mirar si l'usuari ha enviat un objecte de registre o de logIn
                if (aux.getClass().equals(Register.class)){
                    logged = ((Register)aux).checkSignIn();
                    //TODO enviar missatge d'error en cas que sigui incorrecte
                }else if (aux.getClass().equals(LogIn.class)) {
                    logged = ((LogIn)aux).checkLogIn();
                    //TODO enviar missatge d'error en cas que sigui incorrecte
                }

            }

            /*objectOut.reset();

            //TODO recuperar l'array de projectes de la BBDD
            objectOut.writeObject("so");*/

            while(isOn) {
                aux = objectIn.readObject();
                if (aux.getClass().equals(String.class)){
                    this.hash = (String)aux;
                    if (projectServers.containsKey(hash)) {
                        projectServers.get(hash).add(this);
                    }else {
                        final LinkedList<DedicatedServer> newlist = new LinkedList<>();
                        newlist.add(this);
                        projectServers.put(hash, newlist);
                    }
                }else {
                    //TODO comprovar les altres classes que es poden passar
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
