package model.user;

public class UserRanking {

    private String username;
    private int totalTasks;
    private int pendingTasks;

    /**
     * Getter del nom d'usuari del UserRanking.
     * @return Nom d'usuari
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter de les tasques pendents de l'UserRanking.
     * @return Tasques pendents
     */
    public int getPendingTasks() {
        return pendingTasks;
    }

    /**
     * Getter de les tasques total de l'UserRanking.
     * @return Nombre de tasques
     */
    public int getTotalTasks() {
        return totalTasks;
    }

    /**
     * Setter de les tasques pendents de l'UserRanking.
     * @param pendingTasks Tasques pendents
     */
    public void setPendingTasks(int pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    /**
     * Setter de les tasques totals de l'UserRanking.
     * @param totalTasks Nombre de tasques
     */
    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    /**
     * Setter de l'username de l'UserRanking
     * @param username Nom d'usuari
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
