package network.communicators;

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

            if(logIn.checkLogIn()) {
                ds.setUsername(logIn.getUserName());
                ds.sendProjectList();
                provider.addToLoby(ds);
            } else {
                ds.sendData(ServerObjectType.AUTH, 1);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
