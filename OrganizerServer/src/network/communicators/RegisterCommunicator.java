package network.communicators;

import model.ServerObjectType;
import model.user.UserRegister;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;
import network.DedicatedServerProvider;

/**
 * S'encarrega de la communicació quan l'usuari es registra.
 * Envia una resposta depenent de si el registre ha estat satisfactori o no. Hi ha 3 errors possibles.
 */
public class RegisterCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final UserRegister register;
        try {
            register = (UserRegister) ds.readData();

            if(register.checkSignIn() == 0) {
                ds.setUsername(register.getUserName());
                ds.sendProjectList();
                provider.addToLoby(ds);
            } else {
                ds.sendData(ServerObjectType.AUTH, register.checkSignIn());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}