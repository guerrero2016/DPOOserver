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
 * S'encarrega d'escoltar si l'usuari ha sol·licitat un projecte al qual entrar.
 * Envia el projecte sol·licitat
 */
public class ProjectSelectedCommunicator implements Communicable{
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            String hash = ds.readData().toString();
            ds.setHash(hash);
            Project project = DataBaseManager.getProjectDBManager().getProject(ds.getHash());
            provider.addDedicated(hash, ds);
            ds.sendData(ServerObjectType.GET_PROJECT, project);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}