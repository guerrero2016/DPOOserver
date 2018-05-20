package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Task;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la communicació quan un client elimina una tasca.
 * Notifica a tots els clients del projecte.
 */
public class TaskDeletedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final Task task;
        final String categoryID;
        try {
            task = (Task) ds.readData();
            categoryID = ds.readData().toString();
            DataBaseManager.getInstance().getTaskDBManager().deleteTask(task, categoryID);

            provider.sendBroadcast(ds.getHash(), ServerObjectType.DELETE_TASK, task);
            provider.sendBroadcast(ds.getHash(), null, categoryID);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
