package helpers;

import classes.World;
import simulations.Simulation;

import java.io.Serializable;
import java.util.HashMap;

public class HistoryManager implements Serializable {
    private World originalWorld;
    private HashMap<String, Simulation> history;

    public HistoryManager(World world) {
        this.history = new HashMap<>();
        this.originalWorld = world;
    }
    public void addRecord(Simulation simulation){
        history.put(simulation.getUuid().toString(),simulation);
    }
    public Simulation getRecord(String uuid){
        return history.get(uuid);
    }

    public HashMap<String, Simulation> getHistory() {
        return history;
    }

    public World getOriginalWorld() {
        return originalWorld;
    }
}
