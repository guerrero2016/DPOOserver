package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Project;
import model.user.User;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;


import java.io.IOException;
import java.util.ArrayList;

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
            Project project = DataBaseManager.getInstance().getProjectDBManager().getProject(ds.getHash());
            ArrayList<String> membersName = DataBaseManager.getInstance().getMemberDBManager().getMembers(hash);

            for (String m : membersName) {
                if (!m.equals(ds.getUsername())) {
                    project.getUsers().add(new User(m));
                }
            }

            provider.addDedicated(hash, ds);
            provider.deleteFromLobby(ds);
            ds.sendData(ServerObjectType.GET_PROJECT, project);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
