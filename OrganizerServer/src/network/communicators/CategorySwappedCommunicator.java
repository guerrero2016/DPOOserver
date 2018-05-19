package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Category;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicació quan s'intercanvia la posició de dues categories.
 * Notifica a tots els usuaris del projecte.
 */
public class CategorySwappedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            final Category fromCategory = (Category) ds.readData();
            final Category toCategory = (Category) ds.readData();
            DataBaseManager.getInstance().getCategoryDBManager().swapCategory(fromCategory, toCategory);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.SWAP_CATEGORY, fromCategory);
            provider.sendBroadcast(ds.getHash(), null, toCategory);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
