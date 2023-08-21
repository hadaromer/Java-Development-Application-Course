package classes.actions;

import classes.Entity;
import classes.Property;
import classes.World;
import helpers.ExpressionParser;
import exceptions.DivideZeroException;
import exceptions.InvalidTypeException;
import resources.generated.PRDAction;
import validator.Utils;

public class Calculation extends Action {
    public enum CalculationType {MULTIPLY, DIVIDE}
    private String resultProp;
    private String arg1;
    private String arg2;
    private CalculationType calculationType;
    public Calculation(PRDAction action) {
        super(Actions.CALCULATION, action.getEntity());
        this.resultProp = action.getResultProp();
        if(action.getPRDMultiply()!=null){
            calculationType = CalculationType.MULTIPLY;
            this.arg1 = action.getPRDMultiply().getArg1();
            this.arg2 = action.getPRDMultiply().getArg2();
        }
        else {
            calculationType = CalculationType.DIVIDE;
            this.arg1 = action.getPRDDivide().getArg1();
            this.arg2 = action.getPRDDivide().getArg2();
        }
    }

    public String getResultProp() {
        return resultProp;
    }

    public void setResultProp(String resultProp) {
        this.resultProp = resultProp;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public CalculationType getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(CalculationType calculationType) {
        this.calculationType = calculationType;
    }
    public void Act(World world, Entity entity){
        Property p = entity.getProperties().get(this.resultProp);
        String expression1 = ExpressionParser.eval(this.arg1, world, entity);
        String expression2 = ExpressionParser.eval(this.arg2, world, entity);
        if (!Utils.ProperValueForType(p.getType(), expression1)) {
            String source = "Calculation action on " + this.entity + " with value: " + this.arg1;
            throw new InvalidTypeException(Utils.ValueType(expression1), "this calculation", source);
        }
        if (!Utils.ProperValueForType(p.getType(), expression2)) {
            String source = "Calculation action on " + this.entity + " with value: " + this.arg2;
            throw new InvalidTypeException(Utils.ValueType(expression2), "this calculation", source);
        }
        p.setValue(Result(expression1, expression2));
    }

    private String Result(String arg1, String arg2) {
        float res;
        if (calculationType == CalculationType.MULTIPLY) {
            res = Float.parseFloat(arg1) * Float.parseFloat(arg2);
        } else {
            if(Float.parseFloat(arg2) == 0){
                throw new DivideZeroException("Action "+ actionType.toString().toLowerCase() +" on entity "+entity);
            }
            res = Float.parseFloat(arg1) / Float.parseFloat(arg2);
        }
        return res % 1.0 == 0.0 ? String.valueOf((int) res) : String.valueOf(res);
    }

    @Override
    public String toString() {
        return "Calculation{" +
                "resultProp='" + resultProp + '\'' +
                ", arg1='" + arg1 + '\'' +
                ", arg2='" + arg2 + '\'' +
                ", calculationType=" + calculationType +
                ", actionType=" + actionType +
                ", entity='" + entity + '\'' +
                '}';
    }
}
