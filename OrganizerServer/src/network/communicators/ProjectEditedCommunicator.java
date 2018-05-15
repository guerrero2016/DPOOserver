package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Project;
import network.Communicable;
import network.DedicatedServer;

import java.io.IOException;
import java.util.UUID;

/**
 * S'encarrega d'escoltar si algú ha creat o modificat un projecte.
 * La modificació s'envia a tots els membre del projecte
 */
public class ProjectEditedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds) {
        final Project projecte;
        try {
            projecte = (Project) ds.readData();
            if (projecte.getId() == null) {
                String uniqueID = UUID.randomUUID().toString();
                projecte.setId(uniqueID);
            }
            DataBaseManager.getProjectDBManager().addProject(projecte);

            if(projecte.isOwner()) {
                DataBaseManager.getProjectDBManager().addProjectOwner(projecte.getId(), ds.getUsername());
            }

            ds.sendBroadcast(projecte.getId(), ServerObjectType.SET_PROJECT, projecte);
            ds.sendToLobbyMembers(projecte.getId(), ServerObjectType.SET_PROJECT, projecte);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
