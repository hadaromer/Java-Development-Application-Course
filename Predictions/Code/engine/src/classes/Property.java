package classes;

import helpers.RandomCreator;
import resources.generated.PRDEnvProperty;
import resources.generated.PRDProperty;
import validator.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Property implements Serializable {
    private enum PropertyFor {ENVIRONMENT, ENTITY}

    ;
    private final PropertyFor propertyFor;
    private final String name;
    private final String type;
    private final boolean isRange;
    private boolean isRandomInit;
    private float from;
    private float to;
    private String value;
    private int numberOfChanges = 0;
    private List<Integer> changes;

    private int tickValueChanged = 0;

    public Property(Object property) {
        if (property.getClass().getSimpleName().equals(PRDEnvProperty.class.getSimpleName())) {
            propertyFor = PropertyFor.ENVIRONMENT;
            PRDEnvProperty temp = (PRDEnvProperty) property;
            name = temp.getPRDName();
            type = temp.getType();
            isRange = temp.getPRDRange() != null;
            changes = new ArrayList<>();
            if (isRange) {
                from = (float) temp.getPRDRange().getFrom();
                to = (float) temp.getPRDRange().getTo();
            }
        } else {
            propertyFor = PropertyFor.ENTITY;
            PRDProperty temp = (PRDProperty) property;
            name = temp.getPRDName();
            type = temp.getType();
            changes = new ArrayList<>();
            isRange = temp.getPRDRange() != null;
            if (isRange) {
                from = (float) temp.getPRDRange().getFrom();
                to = (float) temp.getPRDRange().getTo();
            }
            isRandomInit = temp.getPRDValue().isRandomInitialize();
            if (isRandomInit) {
                setRandomValue(0);
            } else {
                value = temp.getPRDValue().getInit();
            }
        }
    }

    public Property(Property other, boolean isNew) {
        this.propertyFor = other.propertyFor;
        this.name = other.name;
        this.type = other.type;
        this.isRange = other.isRange;
        this.from = other.from;
        this.to = other.to;
        this.numberOfChanges = other.numberOfChanges;
        this.changes = other.changes;
        if (isNew) {
            this.changes = new ArrayList<>();
        }
        if (isNew && other.isRandomInit) {
            this.isRandomInit = true;
            setRandomValue(0);
        } else {
            this.value = other.value;
        }
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

    public int getTickValueChanged() {
        return tickValueChanged;
    }

    public void setValue(String value, int currentTicks) {
        if (!isRange || Utils.GreaterOrEqual(value, String.valueOf(from)) && Utils.GreaterOrEqual(String.valueOf(to), value)) {
            if (this.value == null || !this.value.equals(value)) {
                this.value = value;
                this.tickValueChanged = currentTicks;
                numberOfChanges++;
                changes.add(currentTicks);
            }
        }
    }

    public void setValueForReplace(String value) {
        if (!isRange || Utils.GreaterOrEqual(value, String.valueOf(from)) && Utils.GreaterOrEqual(String.valueOf(to), value)) {
            this.value = value;
        }
    }

    public void setRandomValue(int currentTicks) {
        switch (type) {
            case "decimal":
                if (isRange) {
                    setValue(String.valueOf(RandomCreator.getInt((int) this.from, (int) this.to)), currentTicks);
                } else {
                    setValue(String.valueOf(RandomCreator.getInt()), currentTicks);
                }
                break;
            case "float":
                if (isRange) {
                    setValue(String.valueOf(RandomCreator.getFloat(from, to)), currentTicks);
                } else {
                    setValue(String.valueOf(RandomCreator.getFloat()), currentTicks);
                }
                break;
            case "boolean":
                setValue(String.valueOf(RandomCreator.getBoolean()), currentTicks);
                break;
            default:
                setValue(RandomCreator.getString(), currentTicks);
                break;
        }
    }

    public boolean validateValue(String value) {
        if (this.isRange()) {
            float temp = Utils.parseFloat(value, "validate value");
            return temp >= this.from && temp <= this.to;
        }
        return true;
    }

    public int getNumberOfChanges() {
        return numberOfChanges;
    }

    public double getConsistency() {
        int sum = 0;
        for (int i = 0; i < changes.size() - 1; i++) {
            sum += changes.get(i + 1) - changes.get(i);
        }
        return (double) sum / changes.size();
    }

    @Override
    public String toString() {
        String res = "Name = " + name + '\n' +
                "Type = " + type + '\n';
        if (isRange) {
            if (type.equals("decimal")) {
                res += "Range: from= " + (int) from + ", to= " + (int) to + '\n';
            } else {
                res += "Range: from= " + from + ", to= " + to + '\n';

            }
        }
        if (propertyFor == Property.PropertyFor.ENTITY) {
            res += "Random initialization: " + isRandomInit + '\n';
        }
        return res;
    }

}
