package model.user;

public class UserRanking {

    private String username;
    private int totalTasks;
    private int pendingTasks;

    public String getUsername() {
        return username;
    }

    public int getPendingTasks() {
        return pendingTasks;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setPendingTasks(int pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
