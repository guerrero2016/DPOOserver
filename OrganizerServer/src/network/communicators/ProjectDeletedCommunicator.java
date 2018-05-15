package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Project;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;
import network.DedicatedServerProvider;

import java.io.IOException;

/**
 * S'encarrega d'escoltar quan s'esborra un projecte.
 * Treu a tots els usuaris que estiguessin dins i l'esborra de la seva llista de projectes.
 */
public class ProjectDeletedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final String projectID;
        try {
            projectID = ds.readData().toString();
            final Project p = DataBaseManager.getProjectDBManager().getProject(projectID);
            DataBaseManager.getProjectDBManager().deleteProject(projectID);
            provider.deleteAllByID(projectID);
            for (String name : DataBaseManager.getMemberDBManager().getMembers(projectID)) {
                provider.sendDataToLobbyUser(name, ServerObjectType.DELETE_PROJECT, p);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}