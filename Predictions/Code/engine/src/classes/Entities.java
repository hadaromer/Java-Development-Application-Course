package classes;

import resources.generated.PRDEntity;
import resources.generated.PRDProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Entities implements Serializable {
    String name;
    int population;
    HashMap<String, Property> properties;
    List<Entity> entities;

    public Entities(PRDEntity entity) {
        this.name = entity.getName();
        this.population = 0;
        this.properties = new HashMap<>();
        this.entities = new ArrayList<>();
        for (PRDProperty property : entity.getPRDProperties().getPRDProperty()) {
            this.properties.put(property.getPRDName(), new Property(property));
        }
    }

    public void initEntitiesList() {
        this.entities = new ArrayList<>();
        for (int i = 0; i < this.population; i++) {
            this.entities.add(new Entity(this.name, properties));
        }
    }

    public void initEntitiesList(Entities other,boolean isNew) {
        this.entities = new ArrayList<>();
        for (int i = 0; i < this.population; i++) {
            this.entities.add(new Entity(other.getEntities().get(i),isNew));
        }
    }

    public Entities(Entities other, boolean isNew) {
        this.name = other.name;
        this.population = other.population;
        this.properties = new HashMap<>();
        for (Property property : other.properties.values()) {
            this.properties.put(property.getName(), new Property(property, isNew));
        }
        initEntitiesList(other, isNew);
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public HashMap<String, Property> getProperties() {
        return properties;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

}
