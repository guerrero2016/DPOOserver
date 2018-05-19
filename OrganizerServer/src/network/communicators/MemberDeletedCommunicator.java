package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.user.User;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicació quan es treu a un encarregat d'un tasca.
 * Es notifica a tots els clients del projecte.
 */
public class MemberDeletedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            final String categoryId = ds.readData().toString();
            final String taskID = ds.readData().toString();
            final User member = (User) ds.readData();
            DataBaseManager.getInstance().getMemberInChargeDBManager().deleteMemberInCharge(member.getUserName(), taskID);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.DELETE_MEMEBER, categoryId);
            provider.sendBroadcast(ds.getHash(), null, taskID);
            provider.sendBroadcast(ds.getHash(), null, member);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
