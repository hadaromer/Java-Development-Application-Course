package classes.dto;

import classes.Rule;
import classes.actions.Action;

import java.util.ArrayList;
import java.util.List;

public class RuleDTO {
    String name;
    List<ActionDTO> actions;
    int ticks = 1;
    float probability = 1;

    public RuleDTO(Rule rule) {
        this.name = rule.getName();
        this.actions = new ArrayList<>();
        for (Action action : rule.getActions()) {
            actions.add(new ActionDTO(action));
        }
        this.ticks = rule.getTicks();
        this.probability = rule.getProbability();
    }

    public String getName() {
        return name;
    }

    public List<ActionDTO> getActions() {
        return actions;
    }

    public int getTicks() {
        return ticks;
    }

    public float getProbability() {
        return probability;
    }

    @Override
    public String toString() {
        return "\n Name = " + name +
                "\n Activation by ticks = " + ticks +
                "\n Activation by probability = " + probability +
                "\n Number of action (top level) = " + actions.size() +
                "\n List of action (top level) = " + actions;

    }
}
