package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.user.UserRegister;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;


/**
 * S'encarrega de la comunicació quan l'usuari es registra.
 * Envia una resposta depenent de si el registre ha estat satisfactori o no. Hi ha 3 errors possibles.
 */
public class RegisterCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        final UserRegister register;
        try {
            register = (UserRegister) ds.readData();

            if(register.checkSignIn() == 0) {
                int error = DataBaseManager.getInstance().getUserDBManager().registrarUsuari(register.getUserName(), register.getEmail(),
                        register.getPassword());
                if (error == 0) {
                    ds.setUsername(register.getUserName());
                    ds.sendData(ServerObjectType.GET_PROJECT_LIST, register.getUserName());
                    ds.sendProjectList();
                    provider.addToLoby(ds);
                } else {
                    ds.sendData(ServerObjectType.AUTH, error);
                }
            } else {
                ds.sendData(ServerObjectType.AUTH, register.checkSignIn());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
