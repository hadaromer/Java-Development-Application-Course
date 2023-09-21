package validator;

import exceptions.InvalidTypeException;

import java.util.HashSet;
import java.util.List;

public class Utils {

    public static String FindFirstDuplicate(List<String> arr) {
        HashSet<String> set = new HashSet<>();
        for (String s : arr) {
            if (!set.add(s)) {
                return s;
            }
        }
        return null;
    }

    public static boolean isContainsSpace(String s) {
        return s.contains(" ");
    }

    public static int parseDecimal(String value, String source) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new InvalidTypeException(ValueType(value)," this action - must be a decimal", source);
        }
    }

    public static float parseFloat(String value, String source) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            throw new InvalidTypeException(ValueType(value)," must be a float", source);
        }
    }

    public static boolean isDecimal(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }

    public static boolean ProperValueForType(String type,String value) {
        switch (type) {
            case "decimal":
                return Utils.isDecimal(value);
            case "float":
                return Utils.isFloat(value);
            case "boolean":
                return isBoolean(value);
            case "string":
                return true;
            default:
                return false;
        }
    }

    public static String ValueType(String value){
        if(isDecimal(value))
            return "decimal";
        if(isFloat(value))
            return "float";
        if(isBoolean(value))
            return "boolean";
        return "string";
    }

    public static boolean isEntityExists(String entity){
        return Validator.entities.get(entity) != null;
    }
    public static boolean isEnvPropertyExists(String envProperty){
        return Validator.envProperties.get(envProperty) != null;
    }
    public static String EnvPropertyType(String envProperty){
        return Validator.envProperties.get(envProperty).getType();
    }
    public static boolean isPropertyExistsInEntity(String entity,String property){
        return Validator.entities.get(entity).get(property) != null;
    }
    public static String PropertyEntityType(String entity,String property){
        return Validator.entities.get(entity).get(property).getType();
    }
    public static boolean GreaterOrEqual(String arg1, String arg2){
        return Float.parseFloat(arg1) >= Float.parseFloat(arg2);
    }
}
