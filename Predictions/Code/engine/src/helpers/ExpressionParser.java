package helpers;

import classes.Entity;
import classes.Property;
import classes.World;
import exceptions.EntityNotFoundException;
import exceptions.EntityPropertyNotFoundException;
import exceptions.EvaluateException;
import validator.HelperFunctionsValidator;
import validator.Utils;

public class ExpressionParser {
    public static String eval(String expression, World world, Entity entity, Entity secondEntity,int currentTicks) {
        String helperFunction = HelperFunctionsValidator.isHelperFunction(expression);
        if (!helperFunction.isEmpty()) { // a helper function
            expression = expression.substring(helperFunction.length() + 1, expression.length() - 1); // remove helper function
            return helperFunctionValue(helperFunction, expression, world, entity, secondEntity,currentTicks);
        } else if (entity.getProperties().get(expression) != null) { // property of entity
            return entity.getProperties().get(expression).getValue();
        }
        return expression;
    }

    public static String helperFunctionValue(String func, String expression, World world, Entity entity,
                                             Entity secondEntity, int currentTicks) {
        switch (func) {
            case "environment":
                return world.getEnvironmentProperties().get(expression).getValue();
            case "random":
                return String.valueOf(RandomCreator.getInt(0, Integer.parseInt(expression)));
            case "evaluate":
                return getEntityPropertyInContext(expression, world, entity, secondEntity).getValue();
            case "percent":
                return PercentParser(expression, world, entity, secondEntity,currentTicks);
            case "ticks":
                return String.valueOf(currentTicks - getEntityPropertyInContext(expression,
                        world, entity, secondEntity).getTickValueChanged());
            default:
                return "";
        }
    }

    private static String PercentParser(String expression, World world, Entity entity, Entity secondEntity,int currentTicks) {
        String wholePart = expression.split(",")[0].trim();
        String percentPart = expression.split(",")[1].trim();
        float percent = Utils.parseFloat(eval(percentPart, world, entity, secondEntity,currentTicks), "Percent") / 100;
        float whole = Utils.parseFloat(eval(wholePart, world, entity, secondEntity,currentTicks), "Percent");
        float res = percent * whole;
        return String.valueOf(res);
    }

    private static Property getEntityPropertyInContext(String expression, World world, Entity entity, Entity secondEntity) {
        String wantedEntity = expression.split("\\.")[0];
        String wantedProperty = expression.split("\\.")[1];
        if (world.getEntities().get(wantedEntity) == null) {
            throw new EntityNotFoundException(wantedEntity, "Evaluate");
        }
        if (wantedEntity.equals(entity.getGroup())) {
            return getEntityProperty(entity, wantedProperty);
        } else if (wantedEntity.equals(secondEntity.getGroup())) {
            return getEntityProperty(secondEntity, wantedProperty);
        } else {
            throw new EvaluateException(wantedEntity, "Evaluate");
        }
    }

    private static Property getEntityProperty(Entity entity, String wantedProperty) {
        Property property = entity.getProperties().get(wantedProperty);
        if (property == null) {
            throw new EntityPropertyNotFoundException(wantedProperty, entity.getGroup(), "Evaluate");
        }
        return property;
    }

}
