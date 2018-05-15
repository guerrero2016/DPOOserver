package network.communicators;

import model.ServerObjectType;
import model.user.UserLogIn;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;
import network.DedicatedServerProvider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * S'encarrega de la communicació quan l'usuari inicia sessió.
 * Envia una resposta depenent si ha sigut satisfactori o no.
 */
public class LogInCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final UserLogIn logIn;
        try {
            logIn = (UserLogIn) ds.readData();
            System.out.println(logIn.getPassword());
            if(logIn.checkLogIn()) {
                ds.setUsername(logIn.getName());
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
