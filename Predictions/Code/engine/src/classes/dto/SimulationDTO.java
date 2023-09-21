package classes.dto;

import simulations.Simulation;
import simulations.SimulationTimer;

import java.util.UUID;

public class SimulationDTO {
    public enum State {INIT, RUNNING, PAUSED,ONE_FORWARD,ONE_PREVIOUS, STOPPED, FINISHED_BY_TICKS, FINISHED_BY_TIME,ERROR}

    private UUID uuid;
    private String startDate;
    private WorldDTO world;
    private int currentTicks;
    private int currentTime;
    private State state;
    private String errorMessage;

    public SimulationDTO(Simulation simulation) {
        this.uuid = simulation.getUuid();
        this.world = new WorldDTO(simulation.getWorld());
        this.currentTicks = simulation.getCurrentTicks();
        this.currentTime = simulation.getTime();
        this.state = SimulationDTO.State.values()[simulation.getState().ordinal()];
        this.startDate = simulation.getStartDate();
        this.errorMessage = simulation.getErrorMessage();
    }

    public UUID getUuid() {
        return uuid;
    }

    public WorldDTO getWorld() {
        return world;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getCurrentTicks() {
        return currentTicks;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public State getState() {
        return state;
    }

    public void updateSimulation(SimulationDTO other) {
        this.world = other.world;
        this.currentTime = other.currentTime;
        this.currentTicks = other.currentTicks;
        this.startDate = other.startDate;
        this.state = other.state;
        this.errorMessage = other.errorMessage;
    }

    public boolean isSameState(SimulationDTO other){
        return this.state == other.state && this.currentTicks == other.currentTicks;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "\nDate = " + startDate +
                "\nUuid = " + uuid;
    }
}
