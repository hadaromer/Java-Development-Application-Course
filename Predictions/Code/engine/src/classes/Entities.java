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
        this.population = entity.getPRDPopulation();
        this.properties = new HashMap<>();
        for (PRDProperty property : entity.getPRDProperties().getPRDProperty()) {
            this.properties.put(property.getPRDName(), new Property(property));
        }
        this.entities = new ArrayList<>();
        for (int i = 0; i < this.population; i++) {
            this.entities.add(new Entity(this.name,i, properties));
        }
    }

    public Entities(Entities other) {
        this.name = other.name;
        this.population = other.population;
        this.properties = new HashMap<>();
        for (Property property : other.properties.values()) {
            this.properties.put(property.getName(), new Property(property));
        }
        this.entities = new ArrayList<>();
        for (int i = 0; i < this.population; i++) {
            this.entities.add(new Entity(other.entities.get(i)));
        }
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

}
