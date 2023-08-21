package validator;

import exceptions.*;

import static validator.Consts.RANGE_TYPES;

public class PropertyValidator {
    public static void PropertyValidator(Property property, String source) {
        if (Utils.isContainsSpace(property.getName()))
            throw new InvalidWhiteSpaceException(property.getName(), source);

        RangeValidator(property, source);
        ValuePropertyValidator(property, source);
    }

    public static void ValuePropertyValidator(Property property, String source) {
        source += ", " + property;
        if (property.getValue() == null) return;

        if (property.getValue().isRandomInitialize()) {
            if (property.getValue().getInit() != null) {
                throw new InvalidRandomInitException(property.getName(),
                        InvalidRandomInitException.Cause.RANDOM_AND_INIT, source);
            }
        } else {
            if (property.getValue().getInit() == null) {
                throw new InvalidRandomInitException(property.getName(),
                        InvalidRandomInitException.Cause.NO_RANDOM_OR_INIT, source);
            }
            ValueAndTypeValidator(property, source);
        }
    }

    public static void ValueAndTypeValidator(Property property, String source) {
        if (!Utils.ProperValueForType(property.getType(), property.getValue().getInit())) {
            throw new InvalidTypeException(property.getType(), "init value", source);
        }
        if(RANGE_TYPES.contains(property.getType())) {
            double value = Double.parseDouble(property.getValue().getInit());
            if (property.getRange() != null
                    && (property.getRange().getFrom() > value
                    || property.getRange().getTo() < value)) {
                throw new ValueNotInRangeException(property.getName(), source);
            }
        }
    }

    public static void RangeValidator(Property property, String source) {
        if (property.getRange() != null) {
            if (!RANGE_TYPES.contains(property.getType())) {
                throw new InvalidTypeException(property.getType(), "range", source);
            } else {
                if (property.getRange().getFrom() > property.getRange().getTo()) {
                    throw new InvalidRangeException(property.getName(),
                            property.getRange().getFrom()+" - "+property.getRange().getTo(),source);
                }
            }
        }
    }
}
