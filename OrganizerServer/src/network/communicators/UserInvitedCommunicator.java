package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Project;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicació quan es convida al projecte.
 * Es notifica a tots els clients del projecte i avisa al client convidat.
 */
public class UserInvitedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final String userName;
        try {
            userName = ds.readData().toString();
            DataBaseManager.getInstance().getMemberDBManager().addMember(ds.getHash(), userName);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.INVITE_USER, userName);
            final Project p = DataBaseManager.getInstance().getProjectDBManager().getProject(ds.getHash());
            provider.sendDataToLobbyUser(userName, ServerObjectType.INVITE_USER, p);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
