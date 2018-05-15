package network.communicators;

import model.ServerObjectType;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

/**
 * S'encarrega de la comunicació quan el client abandona el projecte i torna al selector de projectes.
 * S'envia al client la llista de projectes.
 */
public class ProjectExitCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        provider.deleteDedicated(ds.getHash(), ds);
        ds.setHash(null);
        ds.sendProjectList();
    }
}
