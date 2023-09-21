package classes.dto;

import classes.actions.*;

import java.util.HashMap;
import java.util.Map;

public class ActionDTO {
    private String actionType;
    private String entity;
    private String secondEntity;
    private Map<String, String> args;

    public void commonForAll(Action action) {
        this.actionType = action.getActionType().toString().toLowerCase();
        this.entity = action.getEntity();
        if (action.getSecondayEntity() != null) {
            this.secondEntity = action.getSecondayEntity().getSecondEntity();
        }
        args = new HashMap<>();
    }

    public ActionDTO(Action action) {
        switch (action.getActionType()){
            case INCREASE:
            case DECREASE:
                add((IncreaseDecrease) action);
                break;
            case CONDITION:
                add((ConditionWrapper) action);
                break;
            case SET:
                add((Set) action);
                break;
            case KILL:
                add((Kill) action);
                break;
            case PROXIMITY:
                add((Proximity) action);
                break;
            case REPLACE:
                add((Replace) action);
                break;
            case CALCULATION:
                add((Calculation) action);
        }
    }


    public void add(IncreaseDecrease action) {
        commonForAll(action);
        args.put("Property", action.getProperty());
        args.put("By", action.getBy());
    }

    public void add(Calculation action) {
        commonForAll(action);
        args.put("Type", action.getCalculationType().toString().toLowerCase());
        args.put("Result Property", action.getResultProp());
        args.put("Arg1", action.getArg1());
        args.put("Arg2", action.getArg2());
    }

    public void add(ConditionWrapper action) {
        commonForAll(action);
        args.put("Number of actions then", String.valueOf(action.getActionsThen().size()));
        args.put("Number of actions else", String.valueOf(action.getActionsElse().size()));
        if(action.getRoot().getSingularity() == Condition.Singularity.SINGLE){
            args.put("Operator", action.getRoot().getOperator().toString().toLowerCase());
            args.put("Property", action.getRoot().getProperty());
            args.put("Value", action.getRoot().getValue());
        }
        else {
            args.put("Logic", action.getRoot().getLogical().toString().toLowerCase());
            args.put("Number of conditions", String.valueOf(action.getRoot().getConditions().size()));

        }
    }

    public void add(Kill action) {
        commonForAll(action);
    }

    public void add(Proximity action) {
        commonForAll(action);
        args.put("Target Entity", action.getTargetEntity());
        args.put("Source Entity", action.getSourceEntity());
        args.put("Depth", action.getEnvDepth());
        args.put("Number of actions", String.valueOf(action.getActions().size()));
    }
    public void add(Replace action) {
        commonForAll(action);
        args.put("Kill", action.getKill());
        args.put("Create", action.getCreate());
        args.put("Replace Mode", action.getReplaceMode().toString().toLowerCase());

    }

    public void add(Set action) {
        commonForAll(action);
        args.put("Property", action.getProperty());
        args.put("Value", action.getValue());
    }

    public String getActionType() {
        return actionType;
    }

    public String getEntity() {
        return entity;
    }

    public String getSecondEntity() {
        return secondEntity;
    }

    public Map<String, String> getArgs() {
        return args;
    }
}
