package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Project;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;


import java.io.IOException;

/**
 * S'encarrega de la comunicacnió quan el client sol·licita un projecte al qual entrar.
 * Envia el projecte sol·licitat a l'usuari.
 */
public class ProjectSelectedCommunicator implements Communicable{
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            String hash = ds.readData().toString();
            ds.setHash(hash);
            System.out.println("ENTRA");
            Project project = DataBaseManager.getInstance().getProjectDBManager().getProject(ds.getHash());
            System.out.println(project.getCategories().size());
            provider.addDedicated(hash, ds);
            ds.sendData(ServerObjectType.GET_PROJECT, project);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
