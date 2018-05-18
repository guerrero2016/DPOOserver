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
            System.out.println(logIn.getPassword());
            if(logIn.checkLogIn() && DataBaseManager.getInstance().getUserDBManager().
                    iniciarSessio(logIn.getUserName(), logIn.getPassword()) == 0) {
                ds.setUsername(logIn.getUserName());
                ds.sendProjectList();
                provider.addToLoby(ds);
            } else {
                ds.sendData(ServerObjectType.AUTH, 3);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
