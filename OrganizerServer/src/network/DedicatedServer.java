package network;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.*;
import model.user.UserLogIn;
import model.user.UserRegister;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
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

    public DedicatedServer(Socket sClient, Server server, DedicatedServerProvidable provider) {
        this.isOn = false;
        this.sClient = sClient;
        this.server = server;
        this.hash = null;
        this.provider = provider;
    }

    public String getUsername() {
        return username;
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
        System.out.println("ENTRAA");
        try {
            objectOut = new ObjectOutputStream(sClient.getOutputStream());
            System.out.println("1");
            System.out.println("AQUIII");
            objectIn = new ObjectInputStream(sClient.getInputStream());
            while(isOn) {
                System.out.println("ja o no?");
                int input = objectIn.readInt();
                type =  ServerObjectType.valueOf(input);
                System.out.println(type);
                System.out.println("LATE");
                try {
                    switch (type) {
                        case LOGIN:
                            final UserLogIn logIn = (UserLogIn) objectIn.readObject();
                            if(logIn.checkLogIn()) {
                                username = logIn.getUserName();
                                ArrayList<Project> projects = DataBaseManager.
                                        getProjectsInfo(DataBaseManager.getUser(logIn.getUserName()));
                                //TODO Enviar tot l'array de projectes
                            } else {
                                //TODO Enviar missatge de que no es correcte
                            }
                            break;

                        case REGISTER:
                            final UserRegister register = (UserRegister) objectIn.readObject();
                            if(register.checkSignIn() == 0) {
                                username = register.getUserName();
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
                            System.out.println("2");
                            hash = objectIn.readObject().toString();
                            System.out.println(3);
                            Project project = DataBaseManager.getProject(hash);
                            provider.addDedicated(hash, this);
                            sendData(ServerObjectType.GET_PROJECT, project);
                            System.out.println(4);
                            break;

                        case SET_PROJECT:
                            System.out.println("Project");
                            String uniqueID = UUID.randomUUID().toString();
                            final Project projecte = (Project) objectIn.readObject();
                            projecte.setId(uniqueID);
                            DataBaseManager.addProject(projecte);
                            sendData(ServerObjectType.SET_PROJECT, projecte);
                            System.out.println("Broadcast");
                            break;

                        case DELETE_PROJECT:
                            final String projectID = objectIn.readUTF();
                            ArrayList<String> membersName = DataBaseManager.getProject(projectID).getMembersName();
                            DataBaseManager.deleteProject(projectID);
                            provider.deleteAllByID(projectID);
                            for (String name : membersName) {
                                provider.sendBroadcastToUser(name, ServerObjectType.DELETE_PROJECT, projectID);
                            }
                            break;

                        case SET_CATEGORY:
                            final Category category = (Category) objectIn.readObject();
                            DataBaseManager.addCategory(category, hash);
                            provider.sendBroadcast(hash, category);
                            break;

                        case DELETE_CATEGORY:
                            final String categoryID = objectIn.readUTF();
                            //TODO obtenir nom de la categoria
                            DataBaseManager.deleteCategory(hash, categoryID);
                            provider.sendBroadcast(hash, categoryID);
                            break;

                        case SET_TAG:
                            final Tag tag = (Tag) objectIn.readObject();
                            DataBaseManager.addTag(tag, hash);
                            provider.sendBroadcast(hash, tag);
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
                            final MemberInCharge encarregat = (MemberInCharge) objectIn.readObject();
                            DataBaseManager.addEncarregat(encarregat, hash);
                            provider.sendBroadcast(hash, encarregat);
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
                            DataBaseManager.addTask(task, hash);
                            provider.sendBroadcast(hash, task);
                            break;

                        case DELETE_TASK:
                            final String taskID = objectIn.readUTF();
                            //TODO
                            String nom_columna3 = "";
                            DataBaseManager.deleteTask(hash, nom_columna3, taskID);
                            provider.sendBroadcast(hash, taskID);
                            break;

                        case SWAP_CATEGORY:
                            final Category category1 = (Category) objectIn.readObject();
                            final Category category2 = (Category) objectIn.readObject();
                            DataBaseManager.swapCategory(hash, category1, category2);
                            break;

                        case SWAP_TASK:
                            final Task task1 = (Task) objectIn.readObject();
                            final Task task2 = (Task) objectIn.readObject();
                            DataBaseManager.swapTask(hash, task1, task2);
                            break;

                        case ADD_USER:
                            final String userName = objectIn.readUTF();
                            DataBaseManager.addMember(hash, userName);
                            provider.sendBroadcast(hash, userName);
                            break;

                        case DELETE_USER:
                            final String username = objectIn.readUTF();
                            DataBaseManager.deleteMember(hash, username);
                            provider.sendBroadcast(hash, username);
                            break;

                        case EXIT_PROJECT:
                            provider.deleteDedicated(hash, this);
                            hash = null;
                            sendData(ServerObjectType.EXIT_PROJECT, null);
                            break;

                        case LOGOUT:
                            provider.deleteDedicated(hash, this);
                            hash = null;
                            sendData(ServerObjectType.LOGOUT, null);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
