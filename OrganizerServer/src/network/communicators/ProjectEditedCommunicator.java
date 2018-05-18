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
    private static final String PATH = "/backgrounds/";
    private static final String EXT = "png";
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

            if (provider.countDedicated(projecte.getId()) == -1){
                ds.sendData(ServerObjectType.SET_PROJECT, projecte);
            }else {
                provider.sendBroadcast(projecte.getId(), ServerObjectType.SET_PROJECT, projecte);
            }

            for (String name : DataBaseManager.getInstance().getMemberDBManager().getMembers(projecte.getId())) {
                System.out.println(projecte.getName());
                provider.sendDataToLobbyUser(name, ServerObjectType.SET_PROJECT, projecte);
            }

            System.out.println("Aqui arriba");

            if (projecte.getBackground() != null) {
                File file = new File(PATH + projecte.getId() + "." + EXT);
                try {
                    ImageIO.write(projecte.getBackground(), EXT, file);  // ignore returned boolean
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
