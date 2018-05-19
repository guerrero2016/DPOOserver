package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Category;
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
        final Category category;
        final String id_projecte;
        try {
            category = (Category) ds.readData();
            id_projecte = (String) ds.readData();
            DataBaseManager.getInstance().getCategoryDBManager().deleteCategory(category, id_projecte);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.DELETE_CATEGORY, category.getId());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
