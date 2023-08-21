package classes.actions;

import classes.Entity;
import classes.World;
import helpers.ExpressionParser;
import exceptions.InvalidTypeException;
import resources.generated.PRDCondition;
import validator.Consts;
import validator.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Condition implements Serializable {
    public enum Singularity {SINGLE, MULTIPLE}
    public enum Operator{EQUALS,NOT_EQUAL,BT,LT}
    public enum Logical{AND,OR}
    private String conditionEntity;
    private Singularity singularity;
    private Operator operator;
    private String property;
    private String value;
    private Logical logical;
    private List<Condition> conditions;

    public Condition(PRDCondition condition){
        if(condition.getSingularity().equals("single")){
            this.singularity = Singularity.SINGLE;
            this.conditionEntity = condition.getEntity();
            this.property = condition.getProperty();
            this.operator = getOperatorByString(condition.getOperator());
            this.value = condition.getValue();
        }
        else{
            this.singularity = Singularity.MULTIPLE;
            this.logical = getLogicalByString(condition.getLogical());
            this.conditions = new ArrayList<>();
            for(PRDCondition cond : condition.getPRDCondition()){
                conditions.add(new Condition(cond));
            }
        }
    }

    private Operator getOperatorByString(String op){
        switch (op){
            case "=":
                return Operator.EQUALS;
            case "!=":
                return Operator.NOT_EQUAL;
            case "bt":
                return Operator.BT;
            case "lt":
                return Operator.LT;
            default:
                return null;
        }
    }
    private Logical getLogicalByString(String logi){
        switch (logi){
            case "or":
                return Logical.OR;
            case "and":
                return Logical.AND;
            default:
                return null;
        }
    }

    public Singularity getSingularity() {
        return singularity;
    }

    public void setSingularity(Singularity singularity) {
        this.singularity = singularity;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
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

    public Logical getLogical() {
        return logical;
    }

    public void setLogical(Logical logical) {
        this.logical = logical;
    }

    public boolean Check(World world, Entity entity){
        if(this.singularity == Singularity.SINGLE){
            return CheckSingle(world,entity);
        }
        else{
            if(logical == Logical.AND){
                return conditions.stream().allMatch(condition -> condition.Check(world,entity));
            }
            else return conditions.stream().anyMatch(condition -> condition.Check(world,entity));
        }
    }

    private boolean CheckSingle(World world, Entity entity){
        String expression =  ExpressionParser.eval(this.value, world, entity);
        String entityValue = entity.getProperties().get(this.property).getValue();
        String entityValueType = entity.getProperties().get(this.property).getType();
       switch (this.operator){
           case EQUALS:
               return entityValue.equalsIgnoreCase(expression);
           case NOT_EQUAL:
               return !entityValue.equalsIgnoreCase(expression);
           case BT:
               ValidateBtLtOperations(entity,expression,Utils.ValueType(expression),
                       entityValue,entityValueType,Operator.BT);
               return Float.parseFloat(entityValue) > Float.parseFloat(expression);
           case LT:
               ValidateBtLtOperations(entity,expression,Utils.ValueType(expression),
                       entityValue,entityValueType,Operator.LT);
               return Float.parseFloat(entityValue) < Float.parseFloat(expression);
           default:
               return false;
       }
    }

    private void ValidateBtLtOperations(Entity entity,String expression,String expressionType,
                                        String entityValue, String entityValueType, Operator operator){
        String source = "Condition "+operator.toString().toLowerCase() + "on " + entity.getGroup();
        if (!Utils.ProperValueForType(entityValueType, expression)) {
            throw new InvalidTypeException(Utils.ValueType(expression), "type of entity's value", source);
        }
        if(!Consts.RANGE_TYPES.contains(entityValueType)){
            throw new InvalidTypeException(entityValueType, "(type of entity value) for this operation " +
                    "only numeric values allowed", source);
        }
        if(!Consts.RANGE_TYPES.contains(Utils.ValueType(expression))){
            throw new InvalidTypeException(Utils.ValueType(expression), "(type of expression) for bt operation " +
                    "only numeric values allowed", source);
        }
    }

    @Override
    public String toString() {
        return "Condition{" +
                "conditionEntity='" + conditionEntity + '\'' +
                ", singularity=" + singularity +
                ", operator=" + operator +
                ", property='" + property + '\'' +
                ", value='" + value + '\'' +
                ", logical=" + logical +
                ", conditions=" + conditions +
                '}';
    }
}
