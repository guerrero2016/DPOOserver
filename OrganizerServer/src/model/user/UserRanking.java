package model.user;

public class UserRanking {

    private String username;
    private int totalTasks;
    private int pendingTasks;

    /**
     * Getter del nom d'usuari del UserRanking.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter de les tasques pendents de l'UserRanking.
     */
    public int getPendingTasks() {
        return pendingTasks;
    }

    /**
     * Getter de les tasques total de l'UserRanking.
     */
    public int getTotalTasks() {
        return totalTasks;
    }

    /**
     * Setter de les tasques pendents de l'UserRanking.
     */
    public void setPendingTasks(int pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    /**
     * Setter de les tasques totals de l'UserRanking.
     */
    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    /**
     * Setter de l'username de l'UserRanking
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
