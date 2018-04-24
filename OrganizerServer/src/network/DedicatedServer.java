package network;

import model.DataBaseManager;
import model.ServerObjectType;
import model.project.*;
import model.user.UserLogIn;
import model.user.UserRegister;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
                            if(logIn.checkLogIn()) {
                                ArrayList<Project> projects = DataBaseManager.getProjectsInfo(DataBaseManager.getUser(logIn.getUserName()));
                                //TODO Enviar tot l'array de projectes
                            } else {
                                //TODO Enviar missatge de que no es correcte
                            }
                            break;

                        case REGISTER:
                            final UserRegister register = (UserRegister) objectIn.readObject();
                            if(register.checkSignIn() == 0) {
                                ArrayList<Project> projects = DataBaseManager.getProjectsInfo(register.getUserName());
                                //TODO Enviar tot l'array de projectes
                            } else if(register.checkSignIn() == 1) {
                                //L'usuari introduit ja existeix a la bbdd
                            } else if(register.checkSignIn() == 2) {
                                //El correu introduit ja existeix a la bbdd
                            } else if(register.checkSignIn() ==3) {
                                //Falla alguna comprovació de les que es fan al client
                            }
                            break;

                        case GET_PROJECT:
                            hash = objectIn.readUTF();
                            Project project = DataBaseManager.getProject(hash);
                            provider.addDedicated(hash, this);
                            //TODO enviar el projecte
                            break;

                        case SET_PROJECT:
                            final Project projecte = (Project) objectIn.readObject();
                            if(DataBaseManager.addProject(projecte)) {
                                provider.sendBroadcast(hash, projecte);
                            } else {
                                //TODO Alguna cosa ha fotut un pet.
                            }
                            break;

                        case DELETE_PROJECT:
                            final String projectID = objectIn.readUTF();
                            DataBaseManager.deleteProject(projectID);
                            provider.deleteAllByID(projectID);
                            break;

                        case SET_CATEGORY:
                            final Category category = (Category) objectIn.readObject();
                            if(DataBaseManager.addCategory(category, hash)) {
                                provider.sendBroadcast(hash, category);
                            } else {
                                //TODO Alguna cosa ha fotut un pet
                            }
                            break;

                        case DELETE_CATEGORY:
                            final String categoryID = objectIn.readUTF();
                            //TODO obtenir nom de la categoria
                            DataBaseManager.deleteCategory(hash, categoryID);
                            provider.sendBroadcast(hash, categoryID);
                            break;

                        case SET_TAG:
                            final Tag tag = (Tag) objectIn.readObject();
                            if(DataBaseManager.addTag(tag, hash, tag.getNomCategoria(), tag.getNomTasca())) {
                                provider.sendBroadcast(hash, tag);
                            } else {
                                //TODO Alguna cosa ha fotut un pet.
                            }
                            break;

                        case DELETE_TAG:
                            //TODO
                            final String nom_tag = objectIn.readUTF();
                            String nom_columna = "";
                            String nom_tasca = "";
                            DataBaseManager.deleteTag(hash, nom_columna, nom_tasca, nom_tag);
                            provider.sendBroadcast(hash, nom_tag);
                            break;

                        case SET_ENCARREGAT:
                            final Encarregat encarregat = (Encarregat) objectIn.readObject();
                            if(DataBaseManager.addEncarregat(encarregat, hash, encarregat.getNomCategoria(), encarregat.getNomTasca())) {
                                provider.sendBroadcast(hash, encarregat);
                            } else {
                                //TODO Alguna cosa ha fotut un pet.
                            }
                            break;

                        case DELETE_ENCARREGAT:
                            //TODO
                            final String nomEncarregat = objectIn.readUTF();
                            String nom_columna2 = "";
                            String nom_tasca2 = "";
                            DataBaseManager.deleteEncarregat(hash, nom_columna2, nom_tasca2, nomEncarregat);
                            provider.sendBroadcast(hash, nomEncarregat);
                            break;

                        case SET_TASK:
                            final Task task = (Task) objectIn.readObject();
                            if(DataBaseManager.addTask(task, hash, task.getNomCategoria())) {
                                provider.sendBroadcast(hash, task);
                            } else {
                                //TODO Alguna cosa ha fotut un pet.
                            }
                            provider.sendBroadcast(hash, task);
                            break;

                        case DELETE_TASK:
                            final String taskID = objectIn.readUTF();
                            //TODO
                            String nom_columna3 = "";
                            DataBaseManager.deleteTask(hash, nom_columna3, taskID);
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
