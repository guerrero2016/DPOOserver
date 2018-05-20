package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicació quan una tasca s'ha acabat.
 * Notifica a tots els clients del projecte.
 */
public class TaskDoneCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final String taskID;
        try {
            taskID = ds.readData().toString();
            System.out.println(taskID);
            DataBaseManager.getInstance().getTaskDBManager().taskDone(taskID);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.TASK_DONE, taskID);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
