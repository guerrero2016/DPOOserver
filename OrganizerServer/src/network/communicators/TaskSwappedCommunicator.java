package network.communicators;

import db.DataBaseManager;
import model.project.Task;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;
import java.util.ArrayList;

/**
 * S'encarrega de la comuncació quan s'intercanvia la posició de dues tasques.
 * Notifica a tots els clients del projecte.
 */
public class TaskSwappedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final ArrayList<Task> tasks;
        try {
            tasks = (ArrayList<Task>) ds.readData();
            DataBaseManager.getInstance().getTaskDBManager().swapTask(tasks);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
