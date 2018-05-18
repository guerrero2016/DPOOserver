package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicació quan s'elimina un usuari del projecte.
 * Es notifica a tots els clients del projecte.
 */
public class UserDeletedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final String username;
        try {
            username = ds.readData().toString();
            DataBaseManager.getInstance().getMemberDBManager().deleteMember(ds.getHash(), username);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.DELETE_USER, username);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
