package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Category;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;
import network.DedicatedServerProvider;

import java.io.IOException;

/**
 * S'encarrega d'escoltar quan s'afegeix o s'edita una categoria.
 * Envia
 */
public class CategoryEditedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final Category category;
        try {
            category = (Category) ds.readData();
            DataBaseManager.getCategoryDBManager().addCategory(category, ds.getHash());
            provider.sendBroadcast(ds.getHash(), ServerObjectType.SET_CATEGORY, category);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
