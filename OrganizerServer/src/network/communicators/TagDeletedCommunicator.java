package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Tag;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicació quan un client elimina una etiqueta.
 * Notifica als altres clients del projecte.
 */
public class TagDeletedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            final String categoryId = ds.readData().toString();
            final String taskId = ds.readData().toString();
            final Tag tag = (Tag) ds.readData();
            DataBaseManager.getInstance().getTagDBManager().deleteTag(tag.getId());
            provider.sendBroadcast(ds.getHash(), ServerObjectType.DELETE_TAG, categoryId);
            provider.sendBroadcast(ds.getHash(), null, taskId);
            provider.sendBroadcast(ds.getHash(), null, tag);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
