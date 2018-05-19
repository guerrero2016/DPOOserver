package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
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
            final String taskID = ds.readData().toString();
            final String memberName = ds.readData().toString();
            DataBaseManager.getInstance().getMemberInChargeDBManager().deleteMemberInCharge(memberName, taskID);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.DELETE_MEMEBER, taskID);
            provider.sendBroadcast(ds.getHash(), null, memberName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
