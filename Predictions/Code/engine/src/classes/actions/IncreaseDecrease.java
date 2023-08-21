package classes.actions;

import classes.Entity;
import classes.Property;
import classes.World;
import helpers.ExpressionParser;
import exceptions.InvalidTypeException;
import resources.generated.PRDAction;
import validator.Utils;

public class IncreaseDecrease extends Action {
    private String property;
    private String by;

    public IncreaseDecrease(PRDAction action) {
        super(Actions.DECREASE, action.getEntity());
        if (action.getType().equals("increase")) {
            setActionType(Actions.INCREASE);
        }
        this.property = action.getProperty();
        this.by = action.getBy();
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public void Act(World world, Entity entity) {
        Property p = entity.getProperties().get(this.property);
        String expression = ExpressionParser.eval(this.by, world, entity);
        if (!Utils.ProperValueForType(p.getType(), expression)) {
            String source = actionType == Actions.INCREASE ? "Increase" : "Decrease";
            source += "action on " + this.entity + " with value: " + this.by;
            throw new InvalidTypeException(Utils.ValueType(expression), "this calculation", source);
        }
        p.setValue(Result(p.getValue(), expression));
    }

    private String Result(String arg1, String arg2) {
        float res;
        if (actionType == Actions.INCREASE) {
            res = Float.parseFloat(arg1) + Float.parseFloat(arg2);
        } else {
            res = Float.parseFloat(arg1) - Float.parseFloat(arg2);
        }
        return res % 1.0 == 0.0 ? String.valueOf((int) res) : String.valueOf(res);
    }

}
