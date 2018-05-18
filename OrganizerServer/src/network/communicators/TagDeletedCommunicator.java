package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
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
        final String tagID;
        try {
            tagID = ds.readData().toString();
            DataBaseManager.getInstance().getTagDBManager().deleteTag(tagID);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.DELETE_TAG, tagID);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
