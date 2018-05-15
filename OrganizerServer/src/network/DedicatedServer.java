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
                int input = objectIn.readInt();
                type =  ServerObjectType.valueOf(input);
                System.out.println(type);
                try {
                    switch (type) {
                        case SET_TAG:
                            final String id_tasca = (String) objectIn.readObject();
                            final Tag tag = (Tag) objectIn.readObject();
                            DataBaseManager.getTagDBManager().addTag(tag, id_tasca);
                            provider.sendBroadcast(hash, ServerObjectType.SET_TAG, tag);
                            break;

                        case DELETE_TAG:
                            //TODO
                            final String id_tag = objectIn.readUTF();
                            DataBaseManager.getTagDBManager().deleteTag(id_tag);
                            break;

                        case SET_ENCARREGAT:
                            final String id_tasca2 = (String) objectIn.readObject();
                            final MemberInCharge encarregat = (MemberInCharge) objectIn.readObject();
                            DataBaseManager.getMemberInChargeDBManager().addMemberInCharge(encarregat, id_tasca2);
                            provider.sendBroadcast(hash, ServerObjectType.SET_ENCARREGAT, encarregat);
                            break;

                        case DELETE_ENCARREGAT:
                            final String id_tasca3 = (String) objectIn.readObject();
                            final String nom_usuari = objectIn.readUTF();
                            DataBaseManager.getMemberInChargeDBManager().deleteMemberInCharge(nom_usuari, id_tasca3);
                            //TODO
                            break;

                        case SET_TASK:
                            final String id_category = (String) objectIn.readObject();
                            final Task task = (Task) objectIn.readObject();
                            DataBaseManager.getTaskDBManager().addTask(task, id_category);
                            provider.sendBroadcast(hash, ServerObjectType.SET_TASK, task);
                            break;

                        case DELETE_TASK:
                            final String taskID = objectIn.readUTF();
                            DataBaseManager.getTaskDBManager().deleteTask(taskID);
                            provider.sendBroadcast(hash, ServerObjectType.DELETE_TASK, taskID);
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
                            provider.sendBroadcast(hash, ServerObjectType.ADD_USER, userName);
                            break;

                        case DELETE_USER:
                            final String username = objectIn.readUTF();
                            DataBaseManager.getMemberDBManager().deleteMember(hash, username);
                            provider.sendBroadcast(hash, ServerObjectType.DELETE_USER, username);
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

                        case TASK_DONE:
                            final String id_task = (String) objectIn.readObject();
                            DataBaseManager.getTaskDBManager().taskDone(id_task);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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