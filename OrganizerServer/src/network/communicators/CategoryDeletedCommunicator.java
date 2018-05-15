package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega d'escoltar quan un usuari elimina una categoria.
 * Envia un missatge per a notificar als altres usuaris del projcte que s'ha eliminat.
 */
public class CategoryDeletedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final String categoryID;
        try {
            categoryID = ds.readData().toString();
            DataBaseManager.getCategoryDBManager().deleteCategory(categoryID);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.DELETE_USER, categoryID);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
