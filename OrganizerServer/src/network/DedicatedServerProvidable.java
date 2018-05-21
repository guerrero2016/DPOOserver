package network;

import model.ServerObjectType;

import java.util.LinkedList;

public interface DedicatedServerProvidable {

    /**
     * Funció que recupera la llista de <code>DedicatedServer</code> que estan al mateix projecte.
     * @param hashCode codi del projecte al que estan
     * @return llista de <code>DedicatedServer</code> que estan al projecte
     */
    LinkedList<DedicatedServer> getDedicatedById(String hashCode);

    /**
     * Procediment que afegeix un <code>DedicatedServer</code> a la llista d'un projecte concret.
     * @param hashCode codi del projecte
     * @param dedicated <code>DedicatedServer</code> a afegir
     */
    void addDedicated(String hashCode, DedicatedServer dedicated);

    /**
     * Funció que recupera quants <code>DedicatedServer</code> hi ha en un projecte
     * @param hashCode codi del projecte
     * @return número de <code>DedicatedServer</code>
     */
    int countDedicated(String hashCode);

    /**
     * Procediment encarregat d'eliminar un <code>DedicatedServer</code> d'un projecte.
     * @param hashCode codi del projecte
     * @param dedicated <code>DedicatedServer</code> a eliminar
     */
    void deleteDedicated(String hashCode, DedicatedServer dedicated);

    /**
     * Procediment encarregat d'enviar dades a tots els clients d'un projecte.
     * @param hashCode codi del projecte
     * @param type mena de dades que s'enviarà
     * @param object objecte a enviar
     */
    void sendBroadcast(String hashCode, ServerObjectType type, Object object);

    /**
     * Proccediment que elimina tots els <code>DedicatedServer</code> d'un projecte
     * @param hashCode codi del projecte
     */
    void deleteAllByID(String hashCode);

    /**
     * Procediment encarregat d'afegir al <code>LOBBY</code> un <code>DedicatedServer</code>
     * @param dedicated Server
     */
    void addToLobby(DedicatedServer dedicated);

    /**
     * Procediment encarregat d'eliminar del <code>LOBBY</code> un <code>DedicatedServer</code>
     * @param dedicated Server
     */
    void deleteFromLobby(DedicatedServer dedicated);

    /**
     * Procediment que envia dades a un usuari del <code>LOBBY</code>
     * @param username usuari al que s'enviaran les dades
     * @param type mena de dades a enviar
     * @param obj objecte a enviar
     */
    void sendDataToLobbyUser(String username, ServerObjectType type, Object obj);

    /**
     * Funció que comprova si existex algun DedicatedServer amb el nom d'usuari que rep
     * @param username Nom d'usuari a comprovar
     * @return true si existeix ja un DS amb el username, false si no.
     */
    boolean checkUserAlreadyConnected(String username);

}
