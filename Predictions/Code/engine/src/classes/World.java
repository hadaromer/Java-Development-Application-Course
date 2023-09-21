package classes;

import classes.actions.Action;
import helpers.RandomCreator;
import resources.generated.PRDEntity;
import resources.generated.PRDEnvProperty;
import resources.generated.PRDRule;
import resources.generated.PRDWorld;

import java.io.Serializable;
import java.util.*;

public class World implements Serializable {
    public enum Direction {UP, DOWN, LEFT, RIGHT};
    private HashMap<String, Property> environmentProperties;
    private HashMap<String, Entities> entities;
    private HashMap<String, Rule> rules;
    private Termination termination;
    private Grid grid;
    private String[][] entitiesLocations;
    private List<Direction> Directions = Arrays.asList(Direction.values());

    public World(PRDWorld world) {
        grid = new Grid(world.getPRDGrid().getRows(), world.getPRDGrid().getColumns());
        entitiesLocations = new String[grid.getRows()][grid.getColumns()];
        environmentProperties = new HashMap<>();
        for (PRDEnvProperty envProperty : world.getPRDEnvironment().getPRDEnvProperty()) {
            environmentProperties.put(envProperty.getPRDName(), new Property(envProperty));
        }

        entities = new HashMap<>();
        for (PRDEntity entity : world.getPRDEntities().getPRDEntity()) {
            entities.put(entity.getName(), new Entities(entity));
        }
        SetInitLocationForEntities();

        rules = new HashMap<>();
        for (PRDRule rule : world.getPRDRules().getPRDRule()) {
            rules.put(rule.getName(), new Rule(rule));
        }

        termination = new Termination(world.getPRDTermination());
        /*int count = 0;
        for(int i=0;i<grid.getRows();i++){
            for(int j=0;j<grid.getColumns();j++){
                if(entitiesLocations[i][j] !=null) {
                    System.out.println(i+"," +j+ ":"+entitiesLocations[i][j]);
                    count++;
                }
            }
        }
        System.out.println(count);*/
    }

    public World(World other, boolean isNew) {
        environmentProperties = new HashMap<>();
        for (Property envProperty : other.getEnvironmentProperties().values()) {
            environmentProperties.put(envProperty.getName(), new Property(envProperty, isNew));
        }

        entities = new HashMap<>();
        for (Entities entity : other.getEntities().values()) {
            entities.put(entity.getName(), new Entities(entity, isNew));
        }

        grid = new Grid(other.getGrid().getRows(), other.getGrid().getColumns());
        entitiesLocations = new String[grid.getRows()][grid.getColumns()];
        if (!isNew) {
            for (int i = 0; i < grid.getRows(); i++) {
                for (int j = 0; j < grid.getColumns(); j++) {
                    entitiesLocations[i][j] = other.getEntitiesLocations()[i][j];
                }
            }
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

    public Grid getGrid() {
        return grid;
    }

    public String[][] getEntitiesLocations() {
        return entitiesLocations;
    }

    public boolean isCellFree(Location location) {
        return entitiesLocations[location.getX()][location.getY()] == null;
    }

    public void setEntityInLocation(String entity, Location location, Location oldLocation) {
        if (oldLocation != null) {
            entitiesLocations[oldLocation.getX()][oldLocation.getY()] = null;
        }
        entitiesLocations[location.getX()][location.getY()] = entity;
    }

    public void moveEntity(String entityGroup,Location currentLocation) {
        Collections.shuffle(Directions); // random directions to try
        Location newLocation = new Location(currentLocation.getX(),currentLocation.getY());
        for (Direction direction : Directions) {
            switch (direction) {
                case UP:
                    newLocation.minusX();
                    if (newLocation.getX() < 0) { // cross from up to bottom
                        newLocation.setX(grid.getRows() - 1);
                    }
                    break;
                case DOWN:
                    newLocation.plusX();
                    if (newLocation.getX() > grid.getRows() - 1) { // cross from bottom to up
                        newLocation.setX(0);
                    }
                    break;
                case LEFT:
                    newLocation.minusY();
                    if (newLocation.getY() < 0) { // cross from bottom to up
                        newLocation.setY(grid.getColumns() - 1);
                    }
                    break;
                case RIGHT:
                    newLocation.plusY();;
                    if (newLocation.getY() > grid.getColumns() - 1) { // cross from bottom to up
                        newLocation.setY(0);
                    }
                    break;
            }
            if (isCellFree(newLocation)) {
                setEntityInLocation(entityGroup,newLocation,currentLocation);
                currentLocation.setX(newLocation.getX());
                currentLocation.setY(newLocation.getY());
                return;
            }
        }
    }

    public void SetInitLocationForEntities() {
        for (Entities ents : entities.values()) {
            for (Entity ent : ents.getEntities()) {
                boolean set = false;
                while (!set) {
                    int x = RandomCreator.getInt(0, grid.getRows());
                    int y = RandomCreator.getInt(0, grid.getColumns());
                    Location location = new Location(x, y);
                    if (isCellFree(location)) {
                        ent.setLocation(x, y);
                        setEntityInLocation(ent.getGroup(), location, null);
                        set = true;
                    }
                }
            }
        }
    }

    public void moveEntities() {
        for (Entities ents : entities.values()) {
            for (Entity ent : ents.getEntities()) {
                moveEntity(ent.getGroup(),ent.getLocation());
            }
        }
    }
}
