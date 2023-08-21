package classes.actions;

import resources.generated.PRDAction;

import java.io.Serializable;

public class ActionFactory implements Serializable {
    public static Action createAction(PRDAction action){
        switch (action.getType()){
            case "increase":
            case "decrease":
                return new IncreaseDecrease(action);
            case "calculation":
                return new Calculation(action);
            case "condition":
                return new ConditionWrapper(action);
            case "set":
                return new Set(action);
            case "kill":
                return new Kill(action);
            default:
                return null;
        }
    }
}
