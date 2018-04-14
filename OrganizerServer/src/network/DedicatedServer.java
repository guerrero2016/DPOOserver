package network;

import model.user.LogIn;
import model.user.Register;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class DedicatedServer extends Thread{

    private boolean isOn;
    private Socket sClient;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private Server server;
    private LinkedList<DedicatedServer> clients;

    public DedicatedServer(Socket sClient, Server server, LinkedList<DedicatedServer> clients) {
        this.isOn = false;
        this.sClient = sClient;
        this.server = server;
        this.clients = clients;
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

            while(isOn) {
                aux = objectIn.readObject();
                //fer un switch per pillar la classe que ha enviat i tractar-ho
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
