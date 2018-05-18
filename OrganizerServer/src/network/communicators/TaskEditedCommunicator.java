package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Task;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;
import java.util.UUID;

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
            if (task.getId() == null || task.getId().isEmpty()) {
                task.setId(UUID.randomUUID().toString());
            }
            System.out.println(categoryID + task.getName() + task.getId());
            DataBaseManager.getInstance().getTaskDBManager().addTask(task, categoryID);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.SET_TASK, task);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
