package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Project;
import model.user.User;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicació quan s'elimina un usuari del projecte.
 * Es notifica a tots els clients del projecte.
 */
public class UserDeletedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final User user;
        try {
            user = (User) ds.readData();
            DataBaseManager.getInstance().getMemberDBManager().deleteMember(ds.getHash(), user.getUserName());
            provider.sendBroadcast(ds.getHash(), ServerObjectType.DELETE_USER, user);
            Project p = DataBaseManager.getInstance().getProjectDBManager().getProject(ds.getHash());
            provider.sendDataToLobbyUser(user.getUserName(), ServerObjectType.DELETE_PROJECT, p);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
