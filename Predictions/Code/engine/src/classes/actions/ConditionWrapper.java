package classes.actions;

import classes.Entity;
import classes.World;
import resources.generated.PRDAction;

import java.util.ArrayList;
import java.util.List;

public class ConditionWrapper extends Action{
    private Condition root;
    private List<Action> actionsThen;
    private List<Action> actionsElse;

    public ConditionWrapper(PRDAction action) {
        super(Actions.CONDITION, action.getEntity(), action.getPRDSecondaryEntity());
        root = new Condition(action.getPRDCondition());
        actionsThen = new ArrayList<>();
        for(PRDAction act : action.getPRDThen().getPRDAction()){
            actionsThen.add(ActionFactory.createAction(act));
        }
        actionsElse = new ArrayList<>();
        if(action.getPRDElse() != null) {
            for (PRDAction act : action.getPRDElse().getPRDAction()) {
                actionsElse.add(ActionFactory.createAction(act));
            }
        }
    }
    public void Act(World world, Entity entity, Entity secondEntity, int currentTicks, List<Runnable> actionsForEndTick){
        boolean conditionResult = root.Check(world,entity,secondEntity,currentTicks);
        if(conditionResult){
            actionsThen.forEach(action -> action.Act(world,entity,secondEntity,currentTicks,actionsForEndTick));
        }
        else {
            actionsElse.forEach(action -> action.Act(world,entity,secondEntity, currentTicks,actionsForEndTick));
        }
    }

    public Condition getRoot() {
        return root;
    }

    public List<Action> getActionsThen() {
        return actionsThen;
    }

    public List<Action> getActionsElse() {
        return actionsElse;
    }
}
