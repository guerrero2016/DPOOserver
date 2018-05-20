package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Project;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;


import java.io.IOException;

/**
 * S'encarrega de la comunicació quan s'esborra un projecte.
 * Treu a tots els usuaris que estiguessin dins i l'esborra de la seva llista de projectes.
 */
public class ProjectDeletedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final String projectID;
        try {
            projectID = ds.readData().toString();
            final Project p = DataBaseManager.getInstance().getProjectDBManager().getProject(projectID);
            for (String name : DataBaseManager.getInstance().getMemberDBManager().getMembers(projectID)) {
                provider.sendDataToLobbyUser(name, ServerObjectType.DELETE_PROJECT, p);
            }
            DataBaseManager.getInstance().getProjectDBManager().deleteProject(projectID);
            provider.deleteAllByID(projectID);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
