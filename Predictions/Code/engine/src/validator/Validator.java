package validator;

import resources.generated.PRDEntity;
import resources.generated.PRDEnvProperty;
import resources.generated.PRDProperty;
import resources.generated.PRDWorld;

import java.util.HashMap;

public class Validator {
    private PRDWorld world;
    protected static HashMap<String, Property> envProperties;
    protected static HashMap<String, HashMap<String, Property>> entities;

    public Validator(PRDWorld _world) {
        world = _world;
    }

    public void Validate() {
        ThreadpoolValidator.ValidateThreadpool(world);
        EnvironmentValidator.ValidateEnvironment(world.getPRDEnvironment());
        GridValidator.ValidateThreadpool(world.getPRDGrid());
        setEnvProperties();
        EntitiesValidator.ValidateEntities(world.getPRDEntities());
        SetEntities();
        RulesValdiator.ValidateRules(world.getPRDRules());
        TerminationValidator.ValidateTermination(world.getPRDTermination());
    }

    public void setEnvProperties() {
        envProperties = new HashMap<>();
        for (PRDEnvProperty prdProperty : world.getPRDEnvironment().getPRDEnvProperty()) {
            envProperties.put(prdProperty.getPRDName(), new Property(prdProperty));
        }
    }

    public void SetEntities() {
        entities = new HashMap<>();
        for (PRDEntity entity : world.getPRDEntities().getPRDEntity()) {
            HashMap<String, Property> temp = new HashMap<>();
            for (PRDProperty prdProperty : entity.getPRDProperties().getPRDProperty()) {
                temp.put(prdProperty.getPRDName(), new Property(prdProperty));
            }
            entities.put(entity.getName(), temp);
        }
    }

}
