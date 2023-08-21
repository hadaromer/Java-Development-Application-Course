package classes;

import resources.generated.PRDEntity;
import resources.generated.PRDEnvProperty;
import resources.generated.PRDRule;
import resources.generated.PRDWorld;

import java.io.Serializable;
import java.util.HashMap;

public class World implements Serializable {
    private HashMap<String, Property> environmentProperties;
    private HashMap<String, Entities> entities;
    private HashMap<String,Rule> rules;
    private Termination termination;

    public World(PRDWorld world){
        environmentProperties = new HashMap<>();
        for(PRDEnvProperty envProperty : world.getPRDEvironment().getPRDEnvProperty()){
            environmentProperties.put(envProperty.getPRDName(),new Property(envProperty));
        }

        entities = new HashMap<>();
        for(PRDEntity entity:world.getPRDEntities().getPRDEntity()){
            entities.put(entity.getName(),new Entities(entity));
        }

        rules = new HashMap<>();
        for(PRDRule rule : world.getPRDRules().getPRDRule()){
            rules.put(rule.getName(),new Rule(rule));
        }

        termination = new Termination(world.getPRDTermination());
    }

    public World(World other){
        environmentProperties = new HashMap<>();
        for(Property envProperty : other.getEnvironmentProperties().values()){
            environmentProperties.put(envProperty.getName(),new Property(envProperty));
        }

        entities = new HashMap<>();
        for(Entities entity:other.getEntities().values()){
            entities.put(entity.getName(),new Entities(entity));
        }

        rules = other.getRules();

        termination = other.getTermination();
    }

    public HashMap<String, Property> getEnvironmentProperties() {
        return environmentProperties;
    }

    public HashMap<String, Entities> getEntities() {
        return entities;
    }

    public HashMap<String, Rule> getRules() {
        return rules;
    }

    public Termination getTermination() {
        return termination;
    }
}
