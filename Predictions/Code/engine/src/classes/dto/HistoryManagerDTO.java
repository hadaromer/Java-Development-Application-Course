package classes.dto;

import helpers.HistoryManager;
import simulations.Simulation;

import java.util.ArrayList;
import java.util.List;

public class HistoryManagerDTO {
    private List<SimulationDTO> history;

    public List<SimulationDTO> getHistory() {
        return history;
    }

    public HistoryManagerDTO(HistoryManager historyManager) {
        history = new ArrayList<>();
        for(Simulation simulation: historyManager.getHistory().values()){
            history.add(new SimulationDTO(simulation));
        }

    }

}
