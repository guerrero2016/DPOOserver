package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
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
        final String taskID;
        try {
            taskID = ds.readData().toString();
            DataBaseManager.getInstance().getTaskDBManager().deleteTask(taskID);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.DELETE_TASK, taskID);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
