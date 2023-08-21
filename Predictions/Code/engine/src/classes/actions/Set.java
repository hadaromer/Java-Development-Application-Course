package classes.actions;

import classes.Entity;
import classes.Property;
import classes.World;
import helpers.ExpressionParser;
import exceptions.InvalidTypeException;
import resources.generated.PRDAction;
import validator.Utils;

public class Set extends Action{
    private String property;
    private String value;

    public Set(PRDAction action) {
        super(Actions.SET, action.getEntity());
        this.property = action.getProperty();
        this.value = action.getValue();
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public void Act(World world, Entity entity) {
        Property p = entity.getProperties().get(this.property);
        String expression = ExpressionParser.eval(this.value, world, entity);
        if (!Utils.ProperValueForType(p.getType(), expression)) {
            String source = "Set action on " + this.entity;
            throw new InvalidTypeException(Utils.ValueType(expression), "this set", source);
        }
        p.setValue(expression);
    }
}
