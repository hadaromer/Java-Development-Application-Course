package simulations;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class SimulationTimer implements Serializable {
    private final long DELAY = 0;
    private final long INTERVAL = 1000;
    int time;
    transient Timer timer;
    transient TimerTask task;

    public SimulationTimer() {
        this.time = 0;
        this.timer = new Timer();
        this.task = new TimerTask() {
            @Override
            public void run() {
                AddOne();
            }
        };
    }

    public void Start(){
        timer.scheduleAtFixedRate(task, DELAY, INTERVAL);
    }

    private void AddOne(){
        this.time++;
    }

    public int getTime() {
        return time;
    }
}
