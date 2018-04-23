package network;

import model.ServerObjectType;

import java.util.LinkedList;

public interface DedicatedServerProvidable {

    LinkedList<DedicatedServer> getDedicatedById(String hashCode);
    void addDedicated(String hashCode, DedicatedServer dedicated);
    int countDedicated(String hashCode);
    void deleteDedicated(String hashCode, DedicatedServer dedicated);
    void sendBroadcast(String hashCode, Object object);
    void deleteAllByID(String hashCode);

}
