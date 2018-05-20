package network.communicators;

import model.ServerObjectType;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;
import java.util.IllegalFormatCodePointException;

/**
 * S'encarrega de la comunicació quan el client decideix sortir del programa o tancar sessió.
 */
public class LogOutCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            ds.readData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (ds.getHash() == null) {
            provider.deleteFromLobby(ds);
        }

        if (ds.getHash() != null) {
            provider.deleteDedicated(ds.getHash(), ds);
            ds.setHash(null);
        }
        if (ds.getUsername() != null) {
            ds.setUsername(null);
        }

    }
}
