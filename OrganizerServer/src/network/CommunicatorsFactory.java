package network;

import model.ServerObjectType;
import network.communicators.LogInCommunicator;
import network.communicators.ProjectDeletedCommunicator;
import network.communicators.RegisterCommunicator;

import java.util.HashMap;

public class CommunicatorsFactory {

    static HashMap<ServerObjectType, Communicable> create () {
        HashMap<ServerObjectType, Communicable> communicators = new HashMap<>();
        communicators.put(ServerObjectType.REGISTER,new RegisterCommunicator());
        communicators.put(ServerObjectType.LOGIN, new LogInCommunicator());
        communicators.put(ServerObjectType.DELETE_PROJECT, new ProjectDeletedCommunicator());
        return communicators;
    }

}
