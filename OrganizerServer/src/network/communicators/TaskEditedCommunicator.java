package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Task;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicació quan un client crea o edita una tasca.
 * Notifica a tots els clients del projecte.
 */
public class TaskEditedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            final String categoryID = ds.readData().toString();
            final Task task = (Task) ds.readData();
            DataBaseManager.getTaskDBManager().addTask(task, categoryID);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.SET_TASK, task);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
