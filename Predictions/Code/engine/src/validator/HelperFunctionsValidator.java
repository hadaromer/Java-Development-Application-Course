package validator;

import exceptions.EntityNotFoundException;
import exceptions.EntityPropertyNotFoundException;
import exceptions.EnvPropertyNotFoundException;
import exceptions.InvalidTypeException;

import javax.rmi.CORBA.Util;
import java.util.Optional;

public class HelperFunctionsValidator {
    public static String isHelperFunction(String expression) {
        Optional<String> helperFunction = Consts.HELPER_FUNCTIONS
                .stream()
                .filter(expression::startsWith)
                .findFirst();
        return helperFunction.orElse("");
    }

    public static String GetFunctionReturnType(String func, String arg, String entity, String source) {
        switch (func) {
            case "environment":
                return EnvironmentValidator(arg, source);
            case "random":
                return RandomValidator(arg, source);
            case "evaluate":
                return EvaluateValidaotr(arg, source);
            case "ticks":
                return TicksValidaotr(arg,source);
            case "percent":
                return PercentValidator(arg, entity, source);
            default:
                return "";
        }
    }

    private static String PercentValidator(String arg, String entity, String source) {
        String[] values = arg.split("\\,");
        for(int i=0;i<2;i++){
            String expression = values[i].trim();
            String type = ActionValidator.getExpressionType(entity,source,expression);
            if (!Consts.RANGE_TYPES.contains(type)) {
                throw new InvalidTypeException(type, "must be a number", source + ", argument "+(i+1));
            }


        }
       return "float";
    }

    private static String EvaluateValidaotr(String arg, String source) {
        String[] values = arg.split("\\.");
        String entity = values[0];
        String property = values[1];

        if (!Utils.isEntityExists(entity)) {
            throw new EntityNotFoundException(entity, source);
        }
        if (!Utils.isPropertyExistsInEntity(entity, property)) {
            throw new EntityPropertyNotFoundException(property, entity, source);
        }
        return Utils.PropertyEntityType(entity, property);
    }

    private static String TicksValidaotr(String arg, String source) {
        String[] values = arg.split("\\.");
        String entity = values[0];
        String property = values[1];

        if (!Utils.isEntityExists(entity)) {
            throw new EntityNotFoundException(entity, source);
        }
        if (!Utils.isPropertyExistsInEntity(entity, property)) {
            throw new EntityPropertyNotFoundException(property, entity, source);
        }
        return "float";
    }

    private static String EnvironmentValidator(String arg, String source) {
        if (!Utils.isEnvPropertyExists(arg)) {
            throw new EnvPropertyNotFoundException(arg, source);
        }
        return Utils.EnvPropertyType(arg);
    }

    private static String RandomValidator(String arg, String source) {
        if (!Utils.isDecimal(arg)) {
            throw new InvalidTypeException(Utils.ValueType(arg), "random init must be a decimal", source);
        }
        return "decimal";
    }
}
