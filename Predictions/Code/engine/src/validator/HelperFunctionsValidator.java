package validator;

import exceptions.EnvPropertyNotFoundException;
import exceptions.InvalidTypeException;

import java.util.Optional;

public class HelperFunctionsValidator {
    public static String isHelperFunction(String expression){
        Optional<String> helperFunction = Consts.HELPER_FUNCTIONS
                .stream()
                .filter(expression::startsWith)
                .findFirst();
        return helperFunction.isPresent() ? helperFunction.get() : "";
    }
    public static String GetFunctionReturnType(String func,String arg,String source){
        switch (func){
            case "environment":
                return EnvironmentValidator(arg,source);
            case "random":
                return RandomValidator(arg,source);
            default:
                return "";
        }
    }

    private static String EnvironmentValidator(String arg,String source){
        if(!Utils.isEnvPropertyExists(arg)){
            throw new EnvPropertyNotFoundException(arg,source);
        }
        return Utils.EnvPropertyType(arg);
    }
    private static String RandomValidator(String arg,String source){
        if(!Utils.isDecimal(arg)){
            throw new InvalidTypeException(Utils.ValueType(arg),"random init must be a decimal",source);
        }
        return "decimal";
    }
}
