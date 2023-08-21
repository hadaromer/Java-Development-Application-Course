package classes;

import helpers.RandomCreator;
import resources.generated.PRDEnvProperty;
import resources.generated.PRDProperty;
import validator.Utils;

import java.io.Serializable;

public class Property implements Serializable {
    private enum PropertyFor {ENVIRONMENT, ENTITY}

    ;
    private PropertyFor propertyFor;
    private String name;
    private String type;
    private boolean isRange;
    private boolean isRandomInit;
    private float from;
    private float to;
    private String value;

    public Property(Object property) {
        if (property.getClass().getSimpleName().equals(PRDEnvProperty.class.getSimpleName())) {
            propertyFor = PropertyFor.ENVIRONMENT;
            PRDEnvProperty temp = (PRDEnvProperty) property;
            name = temp.getPRDName();
            type = temp.getType();
            isRange = temp.getPRDRange() != null;
            if (isRange) {
                from = (float) temp.getPRDRange().getFrom();
                to = (float) temp.getPRDRange().getTo();
            }
        } else {
            propertyFor = PropertyFor.ENTITY;
            PRDProperty temp = (PRDProperty) property;
            name = temp.getPRDName();
            type = temp.getType();
            isRange = temp.getPRDRange() != null;
            if (isRange) {
                from = (float) temp.getPRDRange().getFrom();
                to = (float) temp.getPRDRange().getTo();
            }
            isRandomInit = temp.getPRDValue().isRandomInitialize();
            if (isRandomInit) {
                setRandomValue();
            } else {
                value = temp.getPRDValue().getInit();
            }
        }
    }

    public Property(Property other) {
        this.propertyFor = other.propertyFor;
        this.name = other.name;
        this.type = other.type;
        this.isRange = other.isRange;
        this.from = other.from;
        this.to = other.to;
        this.value = other.value;
    }

    public int getPropertyForOrdinal() {
        return propertyFor.ordinal();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isRange() {
        return isRange;
    }

    public boolean isRandomInit() {
        return isRandomInit;
    }

    public float getFrom() {
        return from;
    }

    public float getTo() {
        return to;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (!isRange || Utils.Greater(value, String.valueOf(from)) && Utils.Greater(String.valueOf(to), value)) {
            this.value = value;
        }
    }

    public void setRandomValue() {
        switch (type) {
            case "decimal":
                if (isRange) {
                    this.value = String.valueOf(RandomCreator.getInt((int) this.from, (int) this.to));
                } else {
                    this.value = String.valueOf(RandomCreator.getInt());
                }
                break;
            case "float":
                if (isRange) {
                    this.value = String.valueOf(RandomCreator.getFloat(from, to));
                } else {
                    this.value = String.valueOf(RandomCreator.getFloat());
                }
                break;
            case "boolean":
                this.value = String.valueOf(RandomCreator.getBoolean());
                break;
            default:
                this.value = RandomCreator.getString();
                break;
        }
    }

    @Override
    public String toString() {
        String res = "Name = " + name + '\n' +
                "Type = " + type + '\n';
        if (isRange) {
            if(type.equals("decimal")) {
                res += "Range: from= " + (int)from + ", to= " + (int)to + '\n';
            }
            else {
                res += "Range: from= " + from + ", to= " + to + '\n';

            }
        }
        if (propertyFor == Property.PropertyFor.ENTITY) {
            res += "Random initialization: " + isRandomInit + '\n';
        }
        return res;
    }

}
