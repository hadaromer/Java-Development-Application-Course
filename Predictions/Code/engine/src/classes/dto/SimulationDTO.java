package classes.dto;

import simulations.Simulation;
import simulations.SimulationTimer;

import java.util.UUID;

public class SimulationDTO {
    public enum State {INIT, RUNNING, PAUSED, STOPPED, FINISHED_BY_TICKS, FINISHED_BY_TIME}

    UUID uuid;
    String startDate;
    WorldDTO world;
    int currentTicks;
    SimulationTimer simulationTimer;
    State state;

    public SimulationDTO(Simulation simulation) {
        this.uuid = simulation.getUuid();
        this.world = new WorldDTO(simulation.getWorld());
        this.currentTicks = simulation.getCurrentTicks();
        this.state = SimulationDTO.State.values()[simulation.getState().ordinal()];
        this.startDate = simulation.getStartDate();
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

    public SimulationTimer getSimulationTimer() {
        return simulationTimer;
    }

    public State getState() {
        return state;
    }

    @Override
    public String toString() {
        return "\nDate = " + startDate +
                "\nUuid = " + uuid;
    }
}
