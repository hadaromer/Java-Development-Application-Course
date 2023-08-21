package classes.dto;

import classes.Property;

public class PropertyDTO {
    public enum PropertyFor {ENVIRONMENT, ENTITY}

    ;
    private PropertyFor propertyFor;
    private String name;
    private String type;
    private boolean isRange;
    private boolean isRandomInit;
    private float from;
    private float to;
    private String value;

    public PropertyDTO(Property other) {
        this.propertyFor = PropertyFor.values()[other.getPropertyForOrdinal()];
        this.name = other.getName();
        this.type = other.getType();
        this.isRange = other.isRange();
        this.isRandomInit = other.isRandomInit();
        this.from = other.getFrom();
        this.to = other.getTo();
        this.value = other.getValue();
    }

    public PropertyFor getPropertyFor() {
        return propertyFor;
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

    @Override
    public String toString() {
        String res = "Name = " + name + '\n' +
                "Type = " + type + '\n';
        if (isRange) {
            if (type.equals("decimal")) {
                res += "Range: from = " + (int) from + ", to = " + (int) to + '\n';
            } else {
                res += "Range: from = " + from + ", to = " + to + '\n';

            }
        }
        if (propertyFor == PropertyFor.ENTITY) {
            res += "Random initialization = " + String.valueOf(isRandomInit) + '\n';
            if (!isRandomInit) {
                res += "Value = " + value + '\n';
            }
        }
        return res;
    }
}
