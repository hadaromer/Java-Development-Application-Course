package classes.actions;

import classes.Entities;
import classes.Entity;
import classes.World;

import java.io.Serializable;

public abstract class Action implements Serializable {
    public enum Actions {INCREASE, DECREASE, CALCULATION, CONDITION, SET, KILL}

    protected Actions actionType;
    protected String entity;

    public Action(Actions actionType, String entity) {
        this.actionType = actionType;
        this.entity = entity;
    }

    public Actions getActionType() {
        return actionType;
    }

    public void setActionType(Actions actionType) {
        this.actionType = actionType;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public void ActOnEntities(World world) {
        Entities entities = world.getEntities().get(this.entity);
        for (Entity ent : entities.getEntities()) {
            Act(world, ent);
        }
    }

    public abstract void Act(World world, Entity entity);

}
