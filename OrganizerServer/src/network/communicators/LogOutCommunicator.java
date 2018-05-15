package network.communicators;

import model.ServerObjectType;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

/**
 * S'encarrega de la comunicació quan el client decideix sortir del programa o tancar sessió.
 */
public class LogOutCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        provider.deleteDedicated(ds.getHash(), ds);
        ds.setHash(null);
        ds.sendData(ServerObjectType.LOGOUT, null);
    }
}
