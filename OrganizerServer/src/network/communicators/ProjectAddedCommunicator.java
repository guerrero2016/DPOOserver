package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Project;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * S'encarrega de la comunicació quan algú crea un projecte.
 */
public class ProjectAddedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final Project projecte;
        try {
            projecte = (Project) ds.readData();
            String uniqueID = UUID.randomUUID().toString();
            projecte.setId(uniqueID);
            projecte.setOwnerName(ds.getUsername());
            DataBaseManager.getInstance().getProjectDBManager().addProject(projecte);
            DataBaseManager.getInstance().getProjectDBManager().addProjectOwner(projecte.getId(), ds.getUsername());
            ds.sendData(ServerObjectType.SET_PROJECT, projecte);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
