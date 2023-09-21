package classes;

import helpers.RandomCreator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class Entity implements Serializable {
    String group;
    UUID entityUuid;
    HashMap<String, Property> properties;

    private Location location;

    public Entity(String group, HashMap<String, Property> properties) {
        this.group = group;
        this.entityUuid = UUID.randomUUID();
        this.location = new Location();
        this.properties = new HashMap<>();
        for (String property : properties.keySet()) {
            this.properties.put(property, new Property(properties.get(property),true));
        }
    }

    public Entity(Entity other, boolean isNew) {
        this.group = other.group;
        this.entityUuid = other.entityUuid;
        this.location = other.location;
        this.properties = new HashMap<>();
        for (Property property : other.properties.values()) {
            this.properties.put(property.getName(), new Property(property,isNew));
        }
    }

    public UUID getUuid() {
        return entityUuid;
    }

    public String getGroup() {
        return group;
    }

    public HashMap<String, Property> getProperties() {
        return properties;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(int x, int y){
        this.location.setX(x);
        this.location.setY(y);
    }

}
