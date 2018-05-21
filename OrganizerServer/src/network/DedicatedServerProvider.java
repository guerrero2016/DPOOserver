package network;

import model.ServerObjectType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class DedicatedServerProvider implements DedicatedServerProvidable{
    private static final String LOBBY = "lobby";

    private HashMap<String, LinkedList<DedicatedServer>> projectServers;

    public DedicatedServerProvider() {
        this.projectServers = new HashMap<>();
        this.projectServers.put(LOBBY, new LinkedList<>());
    }

    @Override
    public LinkedList<DedicatedServer> getDedicatedById(String hashCode) {
        return projectServers.get(hashCode);
    }

    @Override
    public void addDedicated(String hashCode, DedicatedServer dedicated) {
        if(projectServers.containsKey(hashCode)) {
            projectServers.get(hashCode).add(dedicated);
            return;
        }
        final LinkedList<DedicatedServer> newProj = new LinkedList<>();
        newProj.add(dedicated);
        projectServers.put(hashCode, newProj);
    }

    @Override
    public int countDedicated(String hashCode) {
        if (!projectServers.containsKey(hashCode)) {
            return -1;
        }

        return projectServers.get(hashCode).size();
    }

    @Override
    public void deleteDedicated(String hashCode, DedicatedServer dedicated) {
        if (countDedicated(hashCode) == 1) {
            projectServers.remove(hashCode);
            return;
        }
        projectServers.get(hashCode).remove(dedicated);
    }

    @Override
    public void sendBroadcast(String hashCode,ServerObjectType type, Object object) {
        if (projectServers.containsKey(hashCode)) {
            for (DedicatedServer ds : projectServers.get(hashCode)) {
                ds.sendData(type, object);
            }
        }
    }

    @Override
    public void deleteAllByID(String hashCode) {
        if (projectServers.containsKey(hashCode)) {
            sendBroadcast(hashCode, ServerObjectType.GET_PROJECT_LIST, null);
            for (DedicatedServer ds : projectServers.get(hashCode)) {
                ds.sendProjectList();
                addToLobby(ds);
                ds.setHash(null);
            }
            projectServers.remove(hashCode);
        }
    }

    @Override
    public void addToLobby(DedicatedServer dedicated) {
        projectServers.get(LOBBY).add(dedicated);
    }

    @Override
    public void deleteFromLobby(DedicatedServer dedicated) {
        projectServers.get(LOBBY).remove(dedicated);
    }

    @Override
    public void sendDataToLobbyUser(String username, ServerObjectType type, Object obj) {
        for (DedicatedServer ds : projectServers.get(LOBBY)) {
            if (ds.getUsername().equals(username)) {
                ds.sendData(type, obj);
            }
        }
    }

    @Override
    public boolean checkUserAlreadyConnected(String username) {

        //creem una LinkedList on guardarem cada DedicatedServer de forma individual per poder
        //fer les cmprovacions 1 a 1.
        LinkedList<DedicatedServer> allDedicatedsExisting = new LinkedList<>();

        //Recuperem totes les claus del HashMap per poder recuperar els diferents LikedList
        Set<String> keys = projectServers.keySet();


        for(String key: keys){
            //Ara per cada clau recuperem la LinkedList de DedicaedServers associada
            LinkedList<DedicatedServer> partialDedicatedServers = projectServers.get(key);
            //Cada dedicated que forma aquesta LinkedList l'afegim a la LinkedList final
            allDedicatedsExisting.addAll(partialDedicatedServers);
        }

        //Ara que tenim la LinkedList final ja poodem comprovar tots els usernames
        for(DedicatedServer ds: allDedicatedsExisting){
            //En cas que el nom d'usuari del dedicated coincideixi amb el que volem comprovar retornem cert
            if(ds.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }
}
