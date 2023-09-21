package classes.actions;

import classes.Entity;
import classes.World;
import helpers.ExpressionParser;
import resources.generated.PRDAction;
import validator.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Proximity extends Action {
    private String sourceEntity;
    private String targetEntity;
    private String envDepth;
    private List<Action> actions;

    public Proximity(PRDAction action) {
        super(Actions.PROXIMITY, action.getPRDBetween().getSourceEntity(), action.getPRDSecondaryEntity());
        this.sourceEntity = action.getPRDBetween().getSourceEntity();
        this.targetEntity = action.getPRDBetween().getTargetEntity();
        this.envDepth = action.getPRDEnvDepth().getOf();
        actions = new ArrayList<>();
        for (PRDAction act : action.getPRDActions().getPRDAction()) {
            actions.add(ActionFactory.createAction(act));
        }
    }

    public String getSourceEntity() {
        return sourceEntity;
    }

    public void setSourceEntity(String sourceEntity) {
        this.sourceEntity = sourceEntity;
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(String targetEntity) {
        this.targetEntity = targetEntity;
    }

    public String getEnvDepth() {
        return envDepth;
    }

    public void setEnvDepth(String envDepth) {
        this.envDepth = envDepth;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void Act(World world, Entity entity, Entity secondEntity, int currentTicks, List<Runnable> actionsForEndTick) {

        if (secondEntity == null) {
            List<Entity> targetEntities = world.getEntities().get(this.targetEntity).getEntities();
            Optional<Entity> optionalEntity = targetEntities.stream().filter(t -> isClose(world, entity, t, currentTicks)).findFirst();
            optionalEntity.ifPresent(value -> this.actions.forEach(action -> action.Act(world, entity, value, currentTicks,actionsForEndTick)));
        } else {
            if (secondEntity.getGroup().equals(this.targetEntity)) {
                this.actions.forEach(action -> action.Act(world, entity, secondEntity, currentTicks,actionsForEndTick));
            }
        }
    }

    private boolean isClose(World world, Entity source, Entity target, int currentTicks) {
        int distanceX = Math.abs(source.getLocation().getX() - target.getLocation().getX());
        if (distanceX > world.getGrid().getRows() / 2) { // world is round so the entity can go cross
            distanceX = world.getGrid().getRows() -distanceX;
        }
        int distanceY = Math.abs(source.getLocation().getY() - target.getLocation().getY());
        if (distanceY > world.getGrid().getColumns() / 2) {
            distanceY = world.getGrid().getColumns() - distanceY;
        }
        return Math.max(distanceX, distanceY) <= Utils.parseDecimal(ExpressionParser.eval(this.envDepth, world, source, target, currentTicks), "Proximity");
    }
}
