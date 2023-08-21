package validator;

import resources.generated.PRDEnvProperty;
import resources.generated.PRDProperty;
import resources.generated.PRDRange;
import resources.generated.PRDValue;

public class Property {
    protected String name;
    protected String type;
    protected PRDRange range;
    protected PRDValue value;

    public Property(PRDEnvProperty envProperty){
        name = envProperty.getPRDName();
        type = envProperty.getType();
        range = envProperty.getPRDRange();
        value = null;
    }
    public Property(PRDProperty property){
        name = property.getPRDName();
        type = property.getType();
        range = property.getPRDRange();
        value = property.getPRDValue();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public PRDRange getRange() {
        return range;
    }

    public PRDValue getValue() {
        return value;
    }
}
