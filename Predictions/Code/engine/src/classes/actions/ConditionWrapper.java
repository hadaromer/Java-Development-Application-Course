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
        super(Actions.CONDITION, action.getEntity());
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
    public void Act(World world, Entity entity){
        boolean conditionResult = root.Check(world,entity);
        if(conditionResult){
            actionsThen.forEach(action -> action.Act(world,entity));
        }
        else {
            actionsElse.forEach(action -> action.Act(world,entity));
        }
    }
}
