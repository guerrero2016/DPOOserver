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
 * S'encarrega de la comunicació quan algú crea o modifica un projecte.
 * Es notifica a tots els clients del projecte.
 */
public class ProjectEditedCommunicator implements Communicable {
    private static final String PATH = "backgrounds/";
    private static final String EXT = "png";
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final Project project;
        try {

            project = (Project) ds.readData();
            project.setOwner(false);

            if (project.getId() == null) {
                String uniqueID = UUID.randomUUID().toString();
                project.setId(uniqueID);
            }
            DataBaseManager.getInstance().getProjectDBManager().addProject(project);

            if (provider.countDedicated(project.getId()) == -1){
                ds.sendData(ServerObjectType.SET_PROJECT, project);
            }else {
                provider.sendBroadcast(project.getId(), ServerObjectType.SET_PROJECT, project);
            }

            for (String name : DataBaseManager.getInstance().getMemberDBManager().getMembers(project.getId())) {
                provider.sendDataToLobbyUser(name, ServerObjectType.SET_PROJECT, project);
            }

            if (project.getBackground() != null) {
                File file = new File(PATH + project.getId() + "." + EXT);
                try {
                    ImageIO.write(project.getBackground(), EXT, file);  // ignore returned boolean
                } catch(IOException e) {
                    System.out.println("Write error for " + file.getPath() +
                            ": " + e.getMessage());
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
