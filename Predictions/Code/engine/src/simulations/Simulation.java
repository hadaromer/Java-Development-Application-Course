package simulations;

import classes.World;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Simulation implements Serializable {
    public enum State {INIT, RUNNING, PAUSED, STOPPED, FINISHED_BY_TICKS, FINISHED_BY_TIME}

    UUID uuid;
    String startDate;
    World world;
    int currentTicks;
    SimulationTimer simulationTimer;
    State state;

    public Simulation(World world) {
        uuid = UUID.randomUUID();
        this.world = world;
        this.currentTicks = 0;
        simulationTimer = new SimulationTimer();
        state = State.INIT;
    }

    public void Start() {
        this.state = State.RUNNING;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy | HH.mm.ss");
        this.startDate = dateFormat.format(new Date());
        simulationTimer.Start();
        Run();
    }

    public void Pause() {
        this.state = State.PAUSED;
    }

    public void Resume() {
        this.state = State.RUNNING;
    }

    public void Stop() {
        this.state = State.STOPPED;
    }

    private boolean isFinished() {
        if (world.getTermination().getTicks() != -1 && currentTicks > world.getTermination().getTicks()) {
            state = State.FINISHED_BY_TICKS;
            return true;
        }
        if (world.getTermination().getSeconds() != -1 && simulationTimer.getTime() > world.getTermination().getSeconds()) {
            state = State.FINISHED_BY_TIME;
            return true;
        }
        return false;
    }

    private void Run() {
        while (state == State.RUNNING && !isFinished()){
            currentTicks++;
            handleTick();
        }
    }

    private void handleTick(){
        world.getRules().forEach((ruleName,rule)->{
            if(rule.isActive(currentTicks)){
            rule.getActions().forEach(action -> action.ActOnEntities(world));
            }
        });
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getStartDate() {
        return startDate;
    }

    public World getWorld() {
        return world;
    }

    public int getCurrentTicks() {
        return currentTicks;
    }

    public State getState() {
        return state;
    }
}
