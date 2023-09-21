package classes.actions;

import classes.Entities;
import classes.Entity;
import classes.World;
import resources.generated.PRDAction;

import java.util.List;
import java.util.stream.Collectors;

public class Kill extends Action {
    public Kill(PRDAction action) {
        super(Actions.KILL, action.getEntity(), action.getPRDSecondaryEntity());
    }

    public void Act(World world, Entity entity, Entity secondEntity, int currentTicks, List<Runnable> actionsForEndTick) {
        actionsForEndTick.add(() -> {

            Entities entities = world.getEntities().get(entity.getGroup());
            entities.setPopulation(entities.getPopulation() - 1);
            entities.setEntities(entities.getEntities().stream().filter(entity1 ->
                    !entity1.getUuid().equals(entity.getUuid())
            ).collect(Collectors.toList()));
        });
    }
}
