package validator;

import exceptions.EntityNotFoundException;
import exceptions.EntityPropertyNotFoundException;
import exceptions.InvalidTypeException;
import resources.generated.PRDAction;
import resources.generated.PRDCondition;

public class ActionValidator {
    public static void ValidateAction(PRDAction action, String source) {
        source += ", " + action.getType() + " on " + action.getEntity();
        switch (action.getType()) {
            case "increase":
            case "decrease":
                IncreaseDecreaseValidator(action, source);
                break;
            case "calculation":
                CalculationValidator(action, source);
                break;
            case "condition":
                ConditionValidator(action, source);
                break;
            case "set":
                SetValidator(action, source);
                break;
            case "kill":
                KillValidator(action, source);
                break;
        }
    }

    public static void IncreaseDecreaseValidator(PRDAction action, String source) {
        validateEntityExists(action.getEntity(), source);
        validatePropertyExists(action.getEntity(), action.getProperty(), source);
        String propertyType = Utils.PropertyEntityType(action.getEntity(), action.getProperty());
        validatePropertyTypeForMathCalcs(propertyType, source);
        String expressionType = getExpressionType(action.getEntity(), source, action.getBy());
        if (!expressionType.equals(propertyType) && !(expressionType.equals("decimal") && propertyType.equals("float"))) {
            if (expressionType.equals("float") && propertyType.equals("decimal")){
                throw new InvalidTypeException(expressionType, "decimal (can not cast float to decimal)", source);
            }
                throw new InvalidTypeException(expressionType, "math calculations", source);
        }
    }

    private static String getExpressionType(String entity, String source, String expression) {
        String expressionType;
        String helperFunction = HelperFunctionsValidator.isHelperFunction(expression);
        if (!helperFunction.isEmpty()) { // a helper function
            expression = expression.substring(0, expression.length() - 1).split("\\(")[1]; // remove helper function
            expressionType = HelperFunctionsValidator.GetFunctionReturnType(helperFunction, expression, source);
        } else if (Utils.isPropertyExistsInEntity(entity, expression)) { // property of entity
            expressionType = Utils.PropertyEntityType(entity, expression);
        } else {
            expressionType = Utils.ValueType(expression);
        }
        return expressionType;
    }

    private static void validatePropertyTypeForMathCalcs(String propertyType, String source) {
        if (!Consts.RANGE_TYPES.contains(propertyType)) {
            throw new InvalidTypeException(propertyType, "math calculations", source);
        }
    }

    private static void validatePropertyExists(String entity, String property, String source) {
        if (!Utils.isPropertyExistsInEntity(entity, property)) {
            throw new EntityPropertyNotFoundException(property, entity, source);
        }
    }

    private static void validateEntityExists(String entity, String source) {
        if (!Utils.isEntityExists(entity)) {
            throw new EntityNotFoundException(entity, source);
        }
    }

    public static void CalculationValidator(PRDAction action, String source) {
        validateEntityExists(action.getEntity(), source);
        validatePropertyExists(action.getEntity(), action.getResultProp(), source);
        String propertyType = Utils.PropertyEntityType(action.getEntity(), action.getResultProp());
        validatePropertyTypeForMathCalcs(propertyType, source);
        String type1, type2;
        if (action.getPRDMultiply() != null) {
            type1 = getExpressionType(action.getEntity(), source, action.getPRDMultiply().getArg1());
            type2 = getExpressionType(action.getEntity(), source, action.getPRDMultiply().getArg2());
        } else {
            type1 = getExpressionType(action.getEntity(), source, action.getPRDDivide().getArg1());
            type2 = getExpressionType(action.getEntity(), source, action.getPRDDivide().getArg2());
        }
        if (!Consts.RANGE_TYPES.contains(type1)) {
            source += ", arg1";
            throw new InvalidTypeException(type1, "math calculations", source);
        }
        if (!Consts.RANGE_TYPES.contains(type2)) {
            source += ", arg2";
            throw new InvalidTypeException(type2, "math calculations", source);
        }
    }

    public static void ConditionValidator(PRDAction action, String source) {
        validateEntityExists(action.getEntity(), source);
        if (action.getPRDCondition().getSingularity().equals("single")) {
            SingleConditionValidator(action.getPRDCondition(), source);
        } else {
            LoopOverConditions(action.getPRDCondition(), source);
        }
        for (PRDAction prdAction : action.getPRDThen().getPRDAction()) {
            ValidateAction(prdAction, source);
        }
        if(action.getPRDElse() != null) {
            for (PRDAction prdAction : action.getPRDElse().getPRDAction()) {
                ValidateAction(prdAction, source);
            }
        }
    }

    private static void LoopOverConditions(PRDCondition condition, String source) {
        for (PRDCondition prdCondition : condition.getPRDCondition()) {
            if (prdCondition.getSingularity().equals("single")) {
                SingleConditionValidator(prdCondition, source);
            } else {
                LoopOverConditions(prdCondition, source);
            }
        }
    }

    public static void SingleConditionValidator(PRDCondition singleCondition, String source) {
        validateEntityExists(singleCondition.getEntity(), source);
        validatePropertyExists(singleCondition.getEntity(), singleCondition.getProperty(), source);
        String propertyType = Utils.PropertyEntityType(singleCondition.getEntity(), singleCondition.getProperty());
        String expressionType = getExpressionType(singleCondition.getEntity(), source, singleCondition.getValue());
        if (!expressionType.equals(propertyType) && !(expressionType.equals("decimal") && propertyType.equals("float"))) {
            throw new InvalidTypeException(expressionType, "the requested condition - excepted: " + propertyType, source);
        }
        if (Consts.NUMERIC_OPERATORS.contains(singleCondition.getOperator()) && !Consts.RANGE_TYPES.contains(propertyType)) {
            throw new InvalidTypeException(expressionType, "the requested condition - excepted: decimal or float", source);
        }
    }

    public static void SetValidator(PRDAction action, String source) {
        validateEntityExists(action.getEntity(), source);
        validatePropertyExists(action.getEntity(), action.getProperty(), source);
        String propertyType = Utils.PropertyEntityType(action.getEntity(), action.getProperty());
        String expressionType = getExpressionType(action.getEntity(), source, action.getValue());
        if (!expressionType.equals(propertyType) && !(expressionType.equals("decimal") && propertyType.equals("float"))) {
            throw new InvalidTypeException(expressionType, "the requested set - excepted: " + propertyType, source);
        }
    }

    public static void KillValidator(PRDAction action, String source) {
        validateEntityExists(action.getEntity(), source);
    }
}
