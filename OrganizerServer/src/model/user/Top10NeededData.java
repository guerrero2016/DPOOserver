package model.user;

public class Top10NeededData {
    private String username;
    private int totalTasks;
    private int pendingTasks;

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
