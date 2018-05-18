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
<<<<<<< HEAD
            final Project p = DataBaseManager.getInstance().getProjectDBManager().getProject(projectID);
            DataBaseManager.getInstance().getProjectDBManager().deleteProject(projectID);
            provider.deleteAllByID(projectID);
            for (String name : DataBaseManager.getInstance().getMemberDBManager().getMembers(projectID)) {
=======
            System.out.println(projectID);
            final Project p = DataBaseManager.getProjectDBManager().getProject(projectID);
            DataBaseManager.getProjectDBManager().deleteProject(projectID);
            provider.deleteAllByID(projectID);

            provider.sendDataToLobbyUser(ds.getUsername(), ServerObjectType.DELETE_PROJECT, p);

            for (String name : DataBaseManager.getMemberDBManager().getMembers(projectID)) {
>>>>>>> bb3bb61379a2b0c446ce272ce231eab99b2e0881
                provider.sendDataToLobbyUser(name, ServerObjectType.DELETE_PROJECT, p);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
