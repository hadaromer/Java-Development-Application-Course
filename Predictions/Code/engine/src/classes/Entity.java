package classes;

import java.io.Serializable;
import java.util.HashMap;

public class Entity implements Serializable {
    String group;
    int index;
    HashMap<String, Property> properties;

    public Entity(String group,int index, HashMap<String, Property> properties) {
        this.group = group;
        this.index = index;
        this.properties = new HashMap<>();
        for (String property : properties.keySet()) {
            this.properties.put(property, new Property(properties.get(property)));
        }
    }

    public Entity(Entity other) {
        this.group = other.group;
        this.index = other.index;
        this.properties = new HashMap<>();
        for (Property property : other.properties.values()) {
            this.properties.put(property.getName(), new Property(property));
        }
    }

    public int getIndex() {
        return index;
    }

    public String getGroup() {
        return group;
    }

    public HashMap<String, Property> getProperties() {
        return properties;
    }

}
