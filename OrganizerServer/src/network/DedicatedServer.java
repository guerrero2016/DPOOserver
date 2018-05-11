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
                            System.out.println(logIn.checkLogIn());
                            if(logIn.checkLogIn()) {
                                System.out.println("se envia");
                                username = logIn.getUserName();
                                sendProjectList(username);
                            } else {
                                sendData(ServerObjectType.AUTH, 1);
                            }
                            break;

                        case REGISTER:
                            final UserRegister register = (UserRegister) objectIn.readObject();
                            if(register.checkSignIn() == 0) {
                                username = register.getUserName();
                                sendProjectList(username);
                            } else {
                                System.out.println(register.checkSignIn());
                                sendData(ServerObjectType.AUTH, register.checkSignIn());
                                System.out.println("Not nice");
                            }
                            break;

                        case GET_PROJECT:
                            hash = objectIn.readObject().toString();
                            System.out.println(3);
                            Project project = DataBaseManager.getProjectDBManager().getProject(hash);
                            provider.addDedicated(hash, this);
                            sendData(ServerObjectType.GET_PROJECT, project);
                            System.out.println(4);
                            break;

                        case SET_PROJECT:
                            System.out.println("Project");
                            String uniqueID = UUID.randomUUID().toString();
                            final Project projecte = (Project) objectIn.readObject();
                            projecte.setId(uniqueID);
                            DataBaseManager.getProjectDBManager().addProject(projecte);
                            sendData(ServerObjectType.SET_PROJECT, projecte);
                            System.out.println("Broadcast");
                            break;

                        case DELETE_PROJECT:
                            final String projectID = objectIn.readObject().toString();
                            final Project p = DataBaseManager.getProjectDBManager().getProject(projectID);
                            DataBaseManager.getProjectDBManager().deleteProject(projectID);
                            provider.deleteAllByID(projectID);
                            for (String name : p.getMembersName()) {
                                provider.sendDataToLobbyUser(name, ServerObjectType.DELETE_PROJECT, p);
                            }
                            break;

                        case SET_CATEGORY:
                            final Category category = (Category) objectIn.readObject();
                            DataBaseManager.getCategoryDBManager().addCategory(category, hash);
                            provider.sendBroadcast(hash, category);
                            break;

                        case DELETE_CATEGORY:
                            final String categoryID = objectIn.readUTF();
                            //TODO obtenir nom de la categoria
                            DataBaseManager.getCategoryDBManager().deleteCategory(categoryID);
                            provider.sendBroadcast(hash, categoryID);
                            break;

                        case SET_TAG:
                            final Tag tag = (Tag) objectIn.readObject();
                            DataBaseManager.getTagDBManager().addTag(tag);
                            provider.sendBroadcast(hash, tag);
                            break;

                        case DELETE_TAG:
                            //TODO
                            final String id_tag = objectIn.readUTF();
                            DataBaseManager.getTagDBManager().deleteTag(id_tag);
                            break;

                        case SET_ENCARREGAT:
                            final MemberInCharge encarregat = (MemberInCharge) objectIn.readObject();
                            DataBaseManager.getMemberInChargeDBManager().addEncarregat(encarregat);
                            provider.sendBroadcast(hash, encarregat);
                            break;

                        case DELETE_ENCARREGAT:
                            //TODO
                            final String id_encarregat = objectIn.readUTF();
                            DataBaseManager.getMemberInChargeDBManager().deleteEncarregat(id_encarregat);
                            provider.sendBroadcast(hash, id_encarregat);
                            break;

                        case SET_TASK:
                            final Task task = (Task) objectIn.readObject();
                            DataBaseManager.getTaskDBManager().addTask(task);
                            provider.sendBroadcast(hash, task);
                            break;

                        case DELETE_TASK:
                            final String taskID = objectIn.readUTF();
                            DataBaseManager.getTaskDBManager().deleteTask(taskID);
                            provider.sendBroadcast(hash, taskID);
                            break;

                        case SWAP_CATEGORY:
                            final Category category1 = (Category) objectIn.readObject();
                            final Category category2 = (Category) objectIn.readObject();
                            DataBaseManager.getCategoryDBManager().swapCategory(hash, category1, category2);
                            break;

                        case SWAP_TASK:
                            final ArrayList<Task> tasks = (ArrayList<Task>) objectIn.readObject();
                            DataBaseManager.getTaskDBManager().swapTask(tasks);
                            break;

                        case ADD_USER:
                            final String userName = objectIn.readUTF();
                            DataBaseManager.getMemberDBManager().addMember(hash, userName);
                            provider.sendBroadcast(hash, userName);
                            break;

                        case DELETE_USER:
                            final String username = objectIn.readUTF();
                            DataBaseManager.getMemberDBManager().deleteMember(hash, username);
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

    private void sendProjectList(String username) {
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
