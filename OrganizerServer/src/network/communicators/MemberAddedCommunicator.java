package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.MemberInCharge;
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
            final MemberInCharge memberName = (MemberInCharge) ds.readData();
            DataBaseManager.getMemberInChargeDBManager().addMemberInCharge(memberName, taskID);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.SET_MEMBER, memberName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
