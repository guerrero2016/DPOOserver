package network;

import model.ServerObjectType;
import network.communicators.*;

import java.util.HashMap;

/**
 * S'encarrega de posar tots els Communicables en un HashMap
 */
public class CommunicatorsFactory {

    /**
     * Crea un HashMap de Communicables on la clau és el ServerObjectType i hi inicialitza tots els Communicables.
     * @return HashMap amb tots els Communicables creats.
     */
    static HashMap<ServerObjectType, Communicable> create () {
        HashMap<ServerObjectType, Communicable> communicators = new HashMap<>();
        communicators.put(ServerObjectType.DELETE_CATEGORY, new CategoryDeletedCommunicator());
        communicators.put(ServerObjectType.SET_CATEGORY, new CategoryEditedCommunicator());
        communicators.put(ServerObjectType.SWAP_CATEGORY, new CategorySwappedCommunicator());
        communicators.put(ServerObjectType.LOGIN, new LogInCommunicator());
        communicators.put(ServerObjectType.LOGOUT, new LogOutCommunicator());
        communicators.put(ServerObjectType.SET_MEMBER, new MemberAddedCommunicator());
        communicators.put(ServerObjectType.DELETE_MEMEBER, new MemberDeletedCommunicator());
        communicators.put(ServerObjectType.ADD_PROJECT, new ProjectAddedCommunicator());
        communicators.put(ServerObjectType.DELETE_PROJECT, new ProjectDeletedCommunicator());
        communicators.put(ServerObjectType.SET_PROJECT, new ProjectEditedCommunicator());
        communicators.put(ServerObjectType.EXIT_PROJECT, new ProjectExitCommunicator());
        communicators.put(ServerObjectType.GET_PROJECT, new ProjectSelectedCommunicator());
        communicators.put(ServerObjectType.REGISTER, new RegisterCommunicator());
        communicators.put(ServerObjectType.DELETE_TAG, new TagDeletedCommunicator());
        communicators.put(ServerObjectType.SET_TAG, new TagAddedCommunicator());
        communicators.put(ServerObjectType.EDIT_TAG, new TagEditedCommunicator());
        communicators.put(ServerObjectType.DELETE_TASK, new TaskDeletedCommunicator());
        communicators.put(ServerObjectType.TASK_DONE, new TaskDoneCommunicator());
        communicators.put(ServerObjectType.SET_TASK, new TaskEditedCommunicator());
        communicators.put(ServerObjectType.SWAP_TASK, new TaskSwappedCommunicator());
        communicators.put(ServerObjectType.DELETE_USER, new UserDeletedCommunicator());
        communicators.put(ServerObjectType.INVITE_USER, new UserInvitedCommunicator());
        communicators.put(ServerObjectType.JOIN_PROJECT, new UserJoinedCommunicator());
        return communicators;
    }

}
