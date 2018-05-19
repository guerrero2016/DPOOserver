package network.communicators;

import model.ServerObjectType;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicació quan el client abandona el projecte i torna al selector de projectes.
 * S'envia al client la llista de projectes.
 */
public class ProjectExitCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            ds.readData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        provider.deleteDedicated(ds.getHash(), ds);
        provider.addToLoby(ds);
        ds.setHash(null);
        ds.sendData(ServerObjectType.GET_PROJECT_LIST, ds.getUsername());
        ds.sendProjectList();
    }
}
