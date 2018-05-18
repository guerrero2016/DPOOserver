package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicació quan s'elimina una categoria.
 * Notifica a tots els clients del projecte
 */
public class CategoryDeletedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final String categoryID;
        try {
            categoryID = ds.readData().toString();
            DataBaseManager.getInstance().getCategoryDBManager().deleteCategory(categoryID);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.DELETE_USER, categoryID);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
