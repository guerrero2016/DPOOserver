package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface Communicable {
    /**
     * Aquesta funció conté l'acció per dur a terme quan el comunicador és cridat
     * @param ds Servidor Dedicat del client
     * @param provider DedicatedServerProvidable
     */
    void communicate(DedicatedServer ds, DedicatedServerProvidable provider);
}
