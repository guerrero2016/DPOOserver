package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Project;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;


import java.io.IOException;
import java.util.UUID;

/**
 * S'encarrega de la comunicació quan algú crea o modifica un projecte.
 * Es notifica a tots els clients del projecte.
 */
public class ProjectEditedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final Project projecte;
        try {
            projecte = (Project) ds.readData();
            if (projecte.getId() == null) {
                String uniqueID = UUID.randomUUID().toString();
                projecte.setId(uniqueID);
            }
            DataBaseManager.getInstance().getProjectDBManager().addProject(projecte);

            if(projecte.isOwner()) {
                DataBaseManager.getInstance().getProjectDBManager().addProjectOwner(projecte.getId(), ds.getUsername());
            }

            DataBaseManager.getInstance().getMemberDBManager().addMember(projecte.getId(), ds.getUsername());

            if (provider.countDedicated(projecte.getId()) == -1){
                ds.sendData(ServerObjectType.SET_PROJECT, projecte);
            }else {
                provider.sendBroadcast(projecte.getId(), ServerObjectType.SET_PROJECT, projecte);
            }

            for (String name : DataBaseManager.getInstance().getMemberDBManager().getMembers(projecte.getId())) {
                provider.sendDataToLobbyUser(name, ServerObjectType.SET_PROJECT, projecte);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
