package network;

import model.ServerObjectType;
import model.user.UserLogIn;
import model.user.UserRegister;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DedicatedServer extends Thread{

    private boolean isOn;
    private Socket sClient;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private Server server;
    private String hash;
    private DedicatedServerProvidable provider;

    public DedicatedServer(Socket sClient, Server server, DedicatedServerProvidable provider) {
        this.isOn = false;
        this.sClient = sClient;
        this.server = server;
        this.hash = null;
        this.provider = provider;
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
        ServerObjectType type;
        Object object;

        try {
            objectIn = new ObjectInputStream(sClient.getInputStream());
            objectOut = new ObjectOutputStream(sClient.getOutputStream());

            /*while (!logged) {
                aux = objectIn.readObject();
                if (aux.getClass().equals(UserRegister.class)){
                    logged = ((UserRegister)aux).checkSignIn();
                    //TODO enviar missatge d'error en cas que sigui incorrecte
                }else if (aux.getClass().equals(UserLogIn.class)) {
                    logged = ((UserLogIn)aux).checkLogIn();
                    //TODO enviar missatge d'error en cas que sigui incorrecte
                }

            }*/

            /*objectOut.reset();

            //TODO recuperar l'array de projectes de la BBDD
            objectOut.writeObject("so");*/

            while(isOn) {
                type = (ServerObjectType) objectIn.readObject();
                try {
                    switch (type) {
                        case LOGIN:
                            final UserLogIn logIn = (UserLogIn) objectIn.readObject();
                            logIn.checkLogIn();
                            break;

                        case REGISTER:
                            final UserRegister register = (UserRegister) objectIn.readObject();
                            register.checkSignIn();
                            break;

                        case GET_PROJECT:
                            hash = objectIn.readUTF();
                            //TODO agafar PROJECTE de la BBDD
                            sendData("PROJECTE DE BBDD");
                            break;

                        case NEW_PROJECT:
                            break;

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*if (aux.getClass().equals(String.class)){
                    this.hash = (String)aux;
                    /*if (projectServers.containsKey(hash)) {
                        projectServers.get(hash).add(this);
                    }else {
                        final LinkedList<DedicatedServer> newlist = new LinkedList<>();
                        newlist.add(this);
                        projectServers.put(hash, newlist);
                    }
                }else {
                    //TODO comprovar les altres classes que es poden passar
                }*/
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void sendData(Object obj){
        try {
            objectOut.reset();
            objectOut.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
