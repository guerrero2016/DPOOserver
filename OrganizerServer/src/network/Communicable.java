package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface Communicable {
    void communicate(DedicatedServer ds, DedicatedServerProvider provider);
}
