package classes.actions;

import classes.Entities;
import classes.Entity;
import classes.Property;
import classes.World;
import exceptions.EntityNotFoundException;
import resources.generated.PRDAction;

import java.util.List;
import java.util.stream.Collectors;

public class Replace extends Action {
    public enum ReplaceMode {SCRATCH, DERIVED}

    ;
    private String kill;
    private String create;
    private ReplaceMode replaceMode;

    public Replace(PRDAction action) {
        super(Actions.REPLACE, action.getKill(), action.getPRDSecondaryEntity());
        this.kill = action.getKill();
        this.create = action.getCreate();
        this.replaceMode = action.getMode().equals("scratch") ? ReplaceMode.SCRATCH : ReplaceMode.DERIVED;
    }

    public String getKill() {
        return kill;
    }

    public void setKill(String kill) {
        this.kill = kill;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public ReplaceMode getReplaceMode() {
        return replaceMode;
    }

    public void setReplaceMode(ReplaceMode replaceMode) {
        this.replaceMode = replaceMode;
    }

    public void Act(World world, Entity entity, Entity secondEntity, int currentTicks, List<Runnable> actionsForEndTick) {
        actionsForEndTick.add(() -> {

            Entities entitiesToKill = world.getEntities().get(this.kill);
            if (entitiesToKill == null) {
                throw new EntityNotFoundException(this.kill, "Replace");
            }
            entitiesToKill.setPopulation(entitiesToKill.getPopulation() - 1);
            entitiesToKill.setEntities(entitiesToKill.getEntities().stream().filter(entity1 ->
                    !entity1.getUuid().equals(entity.getUuid())
            ).collect(Collectors.toList()));

            Entities entitiesToCreate = world.getEntities().get(this.create);
            if (entitiesToCreate == null) {
                throw new EntityNotFoundException(this.kill, "Replace");
            }
            entitiesToCreate.setPopulation(entitiesToCreate.getPopulation() + 1);
            Entity replaceEntity = new Entity(this.create, entitiesToCreate.getProperties());
            replaceEntity.setLocation(entity.getLocation().getX(), entity.getLocation().getY());
            world.setEntityInLocation(replaceEntity.getGroup(), replaceEntity.getLocation(), null);
            if (this.replaceMode == ReplaceMode.DERIVED) {
                for (Property property : replaceEntity.getProperties().values()) {
                    if (entity.getProperties().get(property.getName()) != null) {
                        property.setValueForReplace(entity.getProperties().get(property.getName()).getValue());
                    }
                }
            }
            entitiesToCreate.addEntity(replaceEntity);
        });
    }
}
