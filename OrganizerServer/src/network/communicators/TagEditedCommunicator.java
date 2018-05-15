package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Tag;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega d'escoltar quan s'ha afegit o modificat una etiqueta.
 * Notifica als altres usuaris.
 */
public class TagEditedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final String id_tasca;
        try {
            id_tasca = ds.readData().toString();
            final Tag tag = (Tag) ds.readData();
            DataBaseManager.getTagDBManager().addTag(tag, id_tasca);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.SET_TAG, tag);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
