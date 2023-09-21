package classes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {

    private ExecutorService executorService;
    private int threadsAmount;

    public ThreadPoolManager() {
        threadsAmount = -1;
    }

    public void setThreadsAmount(int value) {
        threadsAmount = value;
        executorService = Executors.newFixedThreadPool(threadsAmount);
    }

    public void executeTask(Runnable task) {
        executorService.execute(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}