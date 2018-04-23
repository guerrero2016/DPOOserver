package network;

import java.util.HashMap;
import java.util.LinkedList;

public class DedicatedServerProvider implements DedicatedServerProvidable{

    private HashMap<String, LinkedList<DedicatedServer>> projectServers;

    public DedicatedServerProvider() {
        this.projectServers = new HashMap<>();
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
    public void sendBroadcast(String hashCode, Object object) {
        for (DedicatedServer ds : projectServers.get(hashCode)) {
            //TODO funció al Dedicated server d'enviar dades
        }
    }
}
