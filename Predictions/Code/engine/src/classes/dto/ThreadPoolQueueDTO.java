package classes.dto;

public class ThreadPoolQueueDTO {
    private int waitingTasks;
    private int activeThreads;
    private long completedTasks;

    public ThreadPoolQueueDTO(int waitingTasks, int activeThreads, long completedTasks) {
        this.waitingTasks = waitingTasks;
        this.activeThreads = activeThreads;
        this.completedTasks = completedTasks;
    }

    public int getWaitingTasks() {
        return waitingTasks;
    }

    public int getActiveThreads() {
        return activeThreads;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }
}
