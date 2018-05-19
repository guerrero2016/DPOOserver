package network;

import model.ServerObjectType;

import java.util.LinkedList;

public interface DedicatedServerProvidable {

    LinkedList<DedicatedServer> getDedicatedById(String hashCode);
    void addDedicated(String hashCode, DedicatedServer dedicated);
    int countDedicated(String hashCode);
    void deleteDedicated(String hashCode, DedicatedServer dedicated);
    void sendBroadcast(String hashCode, ServerObjectType type, Object object);
    void deleteAllByID(String hashCode);
    void addToLoby(DedicatedServer dedicated);
    void deleteFromLobby(DedicatedServer dedicated);
    void sendDataToLobbyUser(String username, ServerObjectType type, Object obj);

    /**
     * Funció que comprova si existex algun DedicatedServer amb el nom d'usuari que rep
     * @param username Nom d'usuari a comprovar
     * @return true si existeix ja un DS amb el username, false si no.
     */
    boolean checkUserAlreadyConnected(String username);

}
