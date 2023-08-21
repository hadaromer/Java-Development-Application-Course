package classes.dto;

import classes.*;

import java.util.ArrayList;
import java.util.List;

public class WorldDTO {
    private List<PropertyDTO> environmentProperties;
    private List<EntitiesDTO> entities;
    private List<RuleDTO> rules;
    private TerminationDTO termination;

    public WorldDTO(World world) {
        environmentProperties = new ArrayList<>();
        for (Property property : world.getEnvironmentProperties().values()) {
            environmentProperties.add(new PropertyDTO(property));
        }

        entities = new ArrayList<>();
        for (Entities ent : world.getEntities().values()) {
            entities.add(new EntitiesDTO(ent));
        }

        rules = new ArrayList<>();
        for (Rule rule : world.getRules().values()) {
            rules.add(new RuleDTO(rule));
        }

        termination = new TerminationDTO(world.getTermination());
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
}
