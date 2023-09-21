package validator;

import exceptions.*;
import resources.generated.PRDAction;
import resources.generated.PRDCondition;

public class ActionValidator {
    public static void ValidateAction(PRDAction action, String source) {
        source += ", " + action.getType();
        ValidateSecondaryEntity(action.getPRDSecondaryEntity(), source);
        switch (action.getType()) {
            case "increase":
            case "decrease":
                source += " on " + action.getEntity();
                IncreaseDecreaseValidator(action, source);
                break;
            case "calculation":
                source += " on " + action.getEntity();
                CalculationValidator(action, source);
                break;
            case "condition":
                source += " on " + action.getEntity();
                ConditionValidator(action, source);
                break;
            case "set":
                source += " on " + action.getEntity();
                SetValidator(action, source);
                break;
            case "kill":
                source += " on " + action.getEntity();
                KillValidator(action, source);
                break;
            case "replace":
                ReplaceValidator(action, source);
                break;
            case "proximity":
                ProximityValidator(action, source);
                break;
        }
    }

    private static void ProximityValidator(PRDAction action, String source) {
        String sourceEntity = action.getPRDBetween().getSourceEntity();
        String targetEntity = action.getPRDBetween().getTargetEntity();
        String of =action.getPRDEnvDepth().getOf();
        validateEntityExists(sourceEntity, source + " on source entity");
        validateEntityExists(targetEntity, source + " on target entity");
        String expressionType = getExpressionType(sourceEntity,source,of);
        if (!Consts.RANGE_TYPES.contains(expressionType)) {
            throw new InvalidTypeException(expressionType, "must be a decimal", source);
        }
        for(PRDAction act : action.getPRDActions().getPRDAction()){
            ValidateAction(act,source);
        }
    }

    private static void ReplaceValidator(PRDAction action, String source) {
        validateEntityExists(action.getKill(), source + " on kill entity");
        validateEntityExists(action.getCreate(), source + "on create entity");
    }

    public static void ValidateSecondaryEntity(PRDAction.PRDSecondaryEntity secondaryEntity, String source) {
        if (secondaryEntity == null) return;
        source += ", second entity selection on " + secondaryEntity.getEntity();
        validateEntityExists(secondaryEntity.getEntity(), source);
        PRDAction.PRDSecondaryEntity.PRDSelection selection = secondaryEntity.getPRDSelection();
        if (!selection.getCount().equals("ALL") && Integer.parseInt(selection.getCount()) < 1) {
            throw new InvalidRangeException("count", "1 - infinity", source);
        }
        if (selection.getPRDCondition().getSingularity().equals("single")) {
            SingleConditionValidator(selection.getPRDCondition(), source);
        } else {
            LoopOverConditions(selection.getPRDCondition(), source);
        }
    }

    public static void IncreaseDecreaseValidator(PRDAction action, String source) {
        validateEntityExists(action.getEntity(), source);
        validatePropertyExists(action.getEntity(), action.getProperty(), source);
        String propertyType = Utils.PropertyEntityType(action.getEntity(), action.getProperty());
        validatePropertyTypeForMathCalcs(propertyType, source);
        String expressionType = getExpressionType(action.getEntity(), source, action.getBy());
        if (!expressionType.equals(propertyType) && !(expressionType.equals("decimal") && propertyType.equals("float"))) {
            if (expressionType.equals("float") && propertyType.equals("decimal")) {
                throw new InvalidTypeException(expressionType, "decimal (can not cast float to decimal)", source);
            }
            throw new InvalidTypeException(expressionType, "math calculations", source);
        }
    }

    public static String getExpressionType(String entity, String source, String expression) {
        String expressionType;
        String helperFunction = HelperFunctionsValidator.isHelperFunction(expression);
        if (!helperFunction.isEmpty()) { // a helper function
            expression = expression.substring(helperFunction.length()+1, expression.length() - 1); // remove helper function
            expressionType = HelperFunctionsValidator.GetFunctionReturnType(helperFunction, expression,entity, source);
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
        if(entity == null){
            throw new EntityNotProvidedException(source);
        }
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
        if (action.getPRDElse() != null) {
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
        String propertyType = getExpressionType(singleCondition.getEntity(), source, singleCondition.getProperty());
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
