package network;

import model.ServerObjectType;
import model.project.Category;
import model.project.Project;
import model.project.Task;
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

            while(isOn) {
                type = (ServerObjectType) objectIn.readObject();
                try {
                    switch (type) {
                        case LOGIN:
                            final UserLogIn logIn = (UserLogIn) objectIn.readObject();
                            logIn.checkLogIn();
                            //TODO Enviar tot l'array de projectes
                            break;

                        case REGISTER:
                            final UserRegister register = (UserRegister) objectIn.readObject();
                            register.checkSignIn();
                            //TODO Enviar tot l'array de projectes
                            break;

                        case GET_PROJECT:
                            hash = objectIn.readUTF();
                            //TODO agafar PROJECTE de la BBDD
                            provider.addDedicated(hash, this);
                            sendData("PROJECTE DE BBDD");
                            break;

                        case SET_PROJECT:
                            final Project project = (Project) objectIn.readObject();
                            //TODO afegir a la BBDD
                            provider.sendBroadcast(hash, project);
                            break;

                        case DELETE_PROJECT:
                            final String projectID = objectIn.readUTF();
                            //TODO delete from BBDD
                            provider.deleteAllByID(projectID);
                            break;

                        case SET_CATEGORY:
                            final Category category = (Category) objectIn.readObject();
                            //TODO afegir o editar de la BBDD
                            provider.sendBroadcast(hash, category);
                            break;

                        case DELETE_CATEGORY:
                            final String categoryID = objectIn.readUTF();
                            //TODO delete from BBDD
                            provider.sendBroadcast(hash, categoryID);
                            break;

                        case SET_TASK:
                            final Task task = (Task) objectIn.readObject();
                            //TODO afegir o editar de la BBDD
                            provider.sendBroadcast(hash, task);
                            break;

                        case DELETE_TASK:
                            final String taskID = objectIn.readUTF();
                            //TODO delete from BBDD
                            provider.sendBroadcast(hash, taskID);
                            break;

                        case ADD_USER:
                            final String userName = objectIn.readUTF();
                            //TODO afegir o editar de la BBDD
                            provider.sendBroadcast(hash, userName);
                            break;

                        case DELETE_USER:
                            final String username = objectIn.readUTF();
                            //TODO afegir o editar de la BBDD
                            provider.sendBroadcast(hash, username);
                            break;

                        case EXIT_PROJECT:
                            provider.deleteDedicated(hash, this);
                            hash = null;
                            sendData(ServerObjectType.EXIT_PROJECT);
                            break;

                        case LOGOUT:
                            provider.deleteDedicated(hash, this);
                            hash = null;
                            sendData(ServerObjectType.LOGOUT);
                            break;

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

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
