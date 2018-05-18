package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Category;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;
import network.DedicatedServerProvider;

import java.io.IOException;
import java.util.UUID;

/**
 * S'encarrega de la comunicació quan s'afegeix o s'edita una categoria.
 * Notifica a tots els clients del projecte
 */
public class CategoryEditedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final Category category;
        try {
            category = (Category) ds.readData();

            if (category.getId() == null || category.getId().isEmpty()) {
                category.setId(UUID.randomUUID().toString());
            }

            DataBaseManager.getInstance().getCategoryDBManager().addCategory(category, ds.getHash());
            provider.sendBroadcast(ds.getHash(), ServerObjectType.SET_CATEGORY, category);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
