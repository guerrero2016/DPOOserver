package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.user.User;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicació quan se li dessigna una tasca a un membre.
 * Notifica als clients del projecte.
 */
public class MemberAddedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            final String taskID = ds.readData().toString();
            final User memberUser = (User) ds.readData();
            DataBaseManager.getMemberInChargeDBManager().addMemberInCharge(memberUser, taskID);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.SET_MEMBER, memberUser);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
