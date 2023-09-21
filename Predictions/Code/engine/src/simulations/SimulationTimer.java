package simulations;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class SimulationTimer implements Serializable {
    private final long DELAY = 0;
    private final long INTERVAL = 1000;
    private int time;
    private transient Timer timer;
    private transient TimerTask task;

    public SimulationTimer() {
        this.time = 0;
        this.timer = new Timer();
        this.task = new TimerTask() {
            @Override
            public void run() {
                addOne();
            }
        };
    }

    public void Start() {
        timer.scheduleAtFixedRate(task, DELAY, INTERVAL);
    }

    public void Pause() {
        task.cancel();
    }

    public void Resume() {
        // Create a new TimerTask and schedule it
        task = new TimerTask() {
            @Override
            public void run() {
                addOne();
            }
        };
        timer.scheduleAtFixedRate(task, DELAY, INTERVAL);
    }

    public void Stop() {
        task.cancel();
        timer.cancel();
    }

    private void addOne() {
        this.time++;
    }

    public int getTime() {
        return time;
    }
}