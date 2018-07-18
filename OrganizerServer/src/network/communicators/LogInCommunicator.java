package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;

import model.user.UserLogIn;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.io.IOException;

/**
 * S'encarrega de la comunicació quan l'usuari inicia sessió.
 * Envia una resposta depenent si ha sigut satisfactori o no.
 */
public class LogInCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final UserLogIn logIn;
        try {
            logIn = (UserLogIn) ds.readData();

            if(logIn.checkLogIn() && DataBaseManager.getInstance().getUserDBManager().
                    iniciarSessio(logIn.getUserName(), logIn.getPassword()) == 0) {
                String username = DataBaseManager.getInstance().getUserDBManager().getUsername(logIn.getUserName());
                if (provider.checkUserAlreadyConnected(username)){
                    ds.sendData(ServerObjectType.AUTH, 4);
                } else {
                    ds.setUsername(username);
                    ds.sendData(ServerObjectType.GET_PROJECT_LIST, username);
                    ds.sendProjectList();
                    provider.addToLobby(ds);
                }
            } else {
                ds.sendData(ServerObjectType.AUTH, 3);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
