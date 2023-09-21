package classes.dto;

import classes.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WorldDTO {
    private GridDTO grid;
    private int maxPopulation;
    private List<PropertyDTO> environmentProperties;
    private List<EntitiesDTO> entities;
    private List<RuleDTO> rules;
    private String[][] entitiesLocations;
    private TerminationDTO termination;

    public WorldDTO(World world) {
        boolean isNew = false;
        grid = new GridDTO(world.getGrid());
        maxPopulation = grid.getColumns() * grid.getRows();
        environmentProperties = new ArrayList<>();
        for (Property property : world.getEnvironmentProperties().values()) {
            environmentProperties.add(new PropertyDTO(property));
        }
        environmentProperties.sort(Comparator.comparing(PropertyDTO::getName));

        entities = new ArrayList<>();
        for (Entities ent : world.getEntities().values()) {
            entities.add(new EntitiesDTO(ent));
        }
        entities.sort(Comparator.comparing(EntitiesDTO::getName));

        rules = new ArrayList<>();
        for (Rule rule : world.getRules().values()) {
            rules.add(new RuleDTO(rule));
        }
        rules.sort(Comparator.comparing(RuleDTO::getName));

        termination = new TerminationDTO(world.getTermination());
        entitiesLocations = new String[grid.getRows()][grid.getColumns()];
        for (int i = 0; i < grid.getRows(); i++) {
            for (int j = 0; j < grid.getColumns(); j++) {
                entitiesLocations[i][j] = world.getEntitiesLocations()[i][j];
            }
        }
    }

    public GridDTO getGrid() {
        return grid;
    }

    public int getMaxPopulation() {
        return maxPopulation;
    }

    public List<PropertyDTO> getEnvironmentProperties() {
        return environmentProperties;
    }

    public List<EntitiesDTO> getEntities() {
        return entities;
    }

    public List<RuleDTO> getRules() {
        return rules;
    }

    public TerminationDTO getTermination() {
        return termination;
    }

    public String[][] getEntitiesLocations() {
        return entitiesLocations;
    }
}
