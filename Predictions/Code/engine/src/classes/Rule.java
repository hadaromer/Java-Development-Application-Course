package classes;

import classes.actions.Action;
import classes.actions.ActionFactory;
import helpers.RandomCreator;
import resources.generated.PRDAction;
import resources.generated.PRDRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rule implements Serializable {
    String name;
    List<Action> actions;
    int ticks = 1;
    float probability = 1;

    public Rule(PRDRule rule) {
        this.name = rule.getName();
        this.actions = new ArrayList<>();
        for (PRDAction action : rule.getPRDActions().getPRDAction()) {
            actions.add(ActionFactory.createAction(action));
        }
        if (rule.getPRDActivation() != null) {
            this.ticks = rule.getPRDActivation().getTicks() != null ? rule.getPRDActivation().getTicks().intValue() : 1;
            this.probability = rule.getPRDActivation().getProbability() != null ? rule.getPRDActivation().getProbability().floatValue() : 1;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public boolean isActive(int currentTicks) {
        return currentTicks % this.ticks == 0 &&
                (this.probability == 1 || this.probability >= RandomCreator.getProbability());
    }

}
