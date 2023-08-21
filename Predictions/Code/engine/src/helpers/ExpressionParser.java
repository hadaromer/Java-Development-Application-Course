package helpers;

import classes.Entity;
import classes.World;
import validator.HelperFunctionsValidator;

public class ExpressionParser {
    public static String eval(String expression, World world, Entity entity) {
        String helperFunction = HelperFunctionsValidator.isHelperFunction(expression);
        if (!helperFunction.isEmpty()) { // a helper function
            expression = expression.substring(0, expression.length() - 1).split("\\(")[1]; // remove helper function
            return helperFunctionValue(helperFunction,expression,world);
        } else if (entity.getProperties().get(expression) != null) { // property of entity
            return entity.getProperties().get(expression).getValue();
        }
        return expression;
    }

    public static String helperFunctionValue(String func,String expression, World world){
        switch (func){
            case "environment":
                return world.getEnvironmentProperties().get(expression).getValue();
            case "random":
                return String.valueOf(RandomCreator.getInt(0,Integer.parseInt(expression)));
            default:
                return "";
        }
    }

}
