package classes.dto;

import classes.Entities;

import java.util.ArrayList;
import java.util.List;

public class EntitiesDTO {
    String name;
    int population;
    List<PropertyDTO> properties;
    List<EntityDTO> entities;

    public EntitiesDTO(Entities other) {
        this.name = other.getName();
        this.population = other.getPopulation();
        this.properties = new ArrayList<>();
        for (String property : other.getProperties().keySet()) {
            this.properties.add(new PropertyDTO(other.getProperties().get(property)));
        }
        this.entities = new ArrayList<>();
        for (int i = 0; i < this.population; i++) {
            this.entities.add(new EntityDTO(other.getEntities().get(i)));
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

    public List<PropertyDTO> getProperties() {
        return properties;
    }

    public List<EntityDTO> getEntities() {
        return entities;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n").append("Name = ").append(name);
        stringBuilder.append("\n").append("Population = ").append(population);
        stringBuilder.append("\n").append("Properties = ").append("\n");
        properties.forEach(p-> stringBuilder.append("-").append(p).append("\n"));
        return stringBuilder.toString();
    }

}
