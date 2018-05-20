package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

public class TaskNotDoneCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            String categoryId = ds.readData().toString();
            final String taskID = ds.readData().toString();
            DataBaseManager.getInstance().getTaskDBManager().taskNotDone(taskID);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.TASK_NOT_DONE, categoryId);
            provider.sendBroadcast(ds.getHash(), null, taskID);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
