package classes.dto;

import classes.Entity;
import classes.Location;

import java.util.HashMap;
import java.util.UUID;

public class EntityDTO {
    String group;
    UUID entityUuid;
    HashMap<String, PropertyDTO> properties;

    private Location location;

    public EntityDTO(Entity other) {
        this.entityUuid = other.getUuid();
        this.location = other.getLocation();
        this.properties = new HashMap<>();
        for (String property : other.getProperties().keySet()) {
            this.properties.put(property, new PropertyDTO(other.getProperties().get(property)));
        }
    }

    public UUID getIndex() {
        return entityUuid;
    }

    public String getGroup() {
        return group;
    }

    public HashMap<String, PropertyDTO> getProperties() {
        return properties;
    }

}
