package classes.dto;

import classes.Entity;

import java.util.HashMap;

public class EntityDTO {
    String group;
    int index;
    HashMap<String, PropertyDTO> properties;

    public EntityDTO(Entity other) {
        this.index = other.getIndex();
        this.properties = new HashMap<>();
        for (String property : other.getProperties().keySet()) {
            this.properties.put(property, new PropertyDTO(other.getProperties().get(property)));
        }
    }

    public int getIndex() {
        return index;
    }

    public String getGroup() {
        return group;
    }

    public HashMap<String, PropertyDTO> getProperties() {
        return properties;
    }

}
