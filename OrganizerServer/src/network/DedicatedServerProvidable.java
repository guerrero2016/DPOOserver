package network;

import model.ServerObject;

import java.util.LinkedList;

public interface DedicatedServerProvidable {

    LinkedList<DedicatedServer> getDedicatedById(String hashCode);
    void addDedicated(String hashCode, DedicatedServer dedicated);
    int countDedicated(String hashCode);
    void deleteDedicated(String hashCode, DedicatedServer dedicated);
    void sendBroadcast(String hashCode, ServerObject object);

}
