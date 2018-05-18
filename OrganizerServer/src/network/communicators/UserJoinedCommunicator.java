package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Project;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicació quan un client entra a un projecte mitjançant el seu ID.
 * Notifica a tots els clients del projecte i envia el projecte al client que acaba d'unir-s'hi.
 */
public class UserJoinedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            final String projectID = ds.readData().toString();
            final Project p = DataBaseManager.getInstance().getProjectDBManager().getProject(projectID);

            DataBaseManager.getMemberDBManager().addMember(projectID, ds.getUsername());
            if (p.getId() != null) {
<<<<<<< HEAD
                DataBaseManager.getInstance().getMemberDBManager().addMember(projectID, ds.getUsername());
=======
>>>>>>> bb3bb61379a2b0c446ce272ce231eab99b2e0881
                provider.sendBroadcast(projectID, ServerObjectType.JOIN_PROJECT, p);
            }
            ds.sendData(ServerObjectType.JOIN_PROJECT, p);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
