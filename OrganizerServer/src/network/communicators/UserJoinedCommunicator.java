package network.communicators;

import db.DataBaseManager;
import db.DataManagers.ProjectDBManager;
import model.ServerObjectType;
import model.project.Project;
import model.user.User;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;
import java.util.ArrayList;

/**
 * S'encarrega de la comunicació quan un client entra a un projecte mitjançant el seu ID.
 * Notifica a tots els clients del projecte i envia el projecte al client que acaba d'unir-s'hi.
 */
public class UserJoinedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            final String projectID = ds.readData().toString();
            final Project p = DataBaseManager.getInstance().getProjectDBManager().getProject(projectID);
            System.out.println(p.getName());
            if (p.getId() != null) {
                DataBaseManager.getInstance().getMemberDBManager().addMember(projectID, ds.getUsername());
                provider.sendBroadcast(projectID, ServerObjectType.JOIN_PROJECT, p);
            }
            ds.sendData(ServerObjectType.SET_PROJECT, p);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Funció que comprova si un usuari ja forma part d'un projecte.
     * @param projectId Identficador del projecte que volem comprovar.
     * @param ds DedicatedServer d'on treurem el nom de l'usuari a comprovar.
     * @return Si ja està al Project retornarem 1, sinó 0.
     */
    public int checkIfAlreadyJoined(String projectId, DedicatedServer ds){
        int exists = 0;
        String userId;
        //recuperem el projecte del que volem comprovar si l'usuari ja en forma part.
        Project projectToCheck = DataBaseManager.getInstance().getProjectDBManager().getProject(projectId);
        //D'aquest proecte recuperem tots els usuaris que en formen part.
        ArrayList<User> usersToCheck = projectToCheck.getUsers();
        //Treiem el User a comprovar
        userId = ds.getUsername();
        //Per cada usuari que forma part del Projecte mirarem si coincideix amb l'usuari a comprovar
        for(User u : usersToCheck){
            if(u.equals(userId)){
                exists = 1;
                return exists;
            }
        }
        return exists;
    }
}
