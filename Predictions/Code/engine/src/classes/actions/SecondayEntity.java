package classes.actions;

import classes.Entities;
import classes.Entity;
import classes.World;
import helpers.RandomCreator;
import resources.generated.PRDAction;
import validator.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SecondayEntity implements Serializable {
    protected String secondEntity;
    protected int count;
    protected boolean allEntities;
    protected Condition condition;

    public SecondayEntity(PRDAction.PRDSecondaryEntity prdSecondaryEntity) {
        this.secondEntity = prdSecondaryEntity.getEntity();
        if (prdSecondaryEntity.getPRDSelection().getCount().equals("ALL")) {
            count = -1;
            allEntities = true;
        } else {
            count = Utils.parseDecimal(prdSecondaryEntity.getPRDSelection().getCount(), "Secondary Entity");
            allEntities = false;
        }
        condition = new Condition(prdSecondaryEntity.getPRDSelection().getPRDCondition());
    }

    public List<Entity> getEntities(World world,int currentTicks){
        Entities entities = world.getEntities().get(secondEntity);
        if(allEntities){
            return entities.getEntities();
        }
        else{
            List<Entity> filteredEntities = new ArrayList<>();
            entities.getEntities().forEach(entity -> {
                if(condition != null) {
                    if (condition.Check(world, entity, null, currentTicks)) {
                        filteredEntities.add(entity);
                    }
                }
                else {
                    filteredEntities.add(entity);
                }
            });
            List<Entity> res = new ArrayList<>();
            int filteredEntitiesSize = filteredEntities.size();
            if(filteredEntitiesSize == 0){
                return res;
            }
            for(int i=0; i<count;i++){
                res.add(filteredEntities.get(RandomCreator.getInt(0,filteredEntitiesSize)));
            }
            return res;
        }
    }

    public String getSecondEntity() {
        return secondEntity;
    }
}
