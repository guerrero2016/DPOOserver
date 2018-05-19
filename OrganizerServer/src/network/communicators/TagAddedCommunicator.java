package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Tag;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicacio quan un client afegeix o  modifica una etiqueta.
 * Notifica als altres usuaris del projecte.
 */
public class TagAddedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            final String taskId = ds.readData().toString();
            final Tag tag = (Tag) ds.readData();
            DataBaseManager.getInstance().getTagDBManager().addTag(tag, taskId);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.SET_TAG, tag);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
