package classes.actions;

import classes.Entity;
import classes.World;
import resources.generated.PRDAction;

import java.io.Serializable;
import java.util.List;

public abstract class Action implements Serializable {
    public enum Actions {INCREASE, DECREASE, CALCULATION, CONDITION, SET, KILL,REPLACE,PROXIMITY}

    protected Actions actionType;
    protected String entity;
    protected SecondayEntity secondayEntity;

    public Action(Actions actionType, String entity, PRDAction.PRDSecondaryEntity prdSecondaryEntity) {
        this.actionType = actionType;
        this.entity = entity;
        if(prdSecondaryEntity!=null){
            secondayEntity = new SecondayEntity(prdSecondaryEntity);
        }
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

    public SecondayEntity getSecondayEntity() {
        return secondayEntity;
    }

    public abstract void Act(World world, Entity entity, Entity secondEntity, int currentTicks, List<Runnable> actionsForEndTick);

}
