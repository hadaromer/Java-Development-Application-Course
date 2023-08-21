import classes.Entities;
import classes.Property;
import classes.World;
import classes.dto.HistoryManagerDTO;
import classes.dto.PropertyDTO;
import classes.dto.WorldDTO;
import helpers.HistoryManager;
import helpers.XmlDeserializer;
import resources.generated.PRDWorld;
import simulations.Simulation;
import validator.Validator;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Engine implements Serializable{
    PRDWorld currentWorld;
    World dummyWorld;
    HistoryManager historyManager;

    public Engine() {
    }

    public boolean isReady() {
        return dummyWorld != null;
    }

    public void LoadXml(String path) throws FileNotFoundException, JAXBException {
        InputStream inputStream = new FileInputStream(path);
        PRDWorld prdWorld = XmlDeserializer.deserialeFrom(inputStream);
        Validator validator = new Validator(prdWorld);
        validator.Validate();
        currentWorld = prdWorld;
        dummyWorld = new World(prdWorld);
        historyManager = new HistoryManager(dummyWorld);
    }

    public WorldDTO GetSimulationDetails() {
        return new WorldDTO(dummyWorld);
    }

    public ArrayList<PropertyDTO> GetEnvProperties() {
        Collection<Property> properties = dummyWorld.getEnvironmentProperties().values();
        ArrayList<PropertyDTO> res = new ArrayList<>();
        for (Property property : properties) {
            res.add(new PropertyDTO(property));
        }
        return res;
    }

    public void SetEnvPropertyByUuid(String uuid, String name, String value) {
        Property p = historyManager.getRecord(uuid).getWorld().getEnvironmentProperties().get(name);
        if (value == null) {
            p.setRandomValue();
        } else p.setValue(value);
    }

    public String CreateNewSimulation() {
        World world = new World(dummyWorld);
        Simulation simulation = new Simulation(world);
        historyManager.addRecord(simulation);
        return simulation.getUuid().toString();
    }

    public String StartSimulation(String uuid) {
        Simulation s = historyManager.getRecord(uuid);
        for (Property property : s.getWorld().getEnvironmentProperties().values()) {
            if (property.getValue() == null)
                property.setRandomValue();
        }
        s.Start();
        return s.getState().name().replace("_", " ").toLowerCase();
    }

    public HistoryManagerDTO GetPastSimulations() {
        return new HistoryManagerDTO(historyManager);
    }

    public HashMap<String, int[]> GetCountOfEntitiesBeforeAndAfter(String uuid) {
        Simulation simulation = historyManager.getRecord(uuid);
        int countEntities = simulation.getWorld().getEntities().values().size();
        HashMap<String, int[]> res = new HashMap<>();
        int[][] temp = new int[countEntities][2]; // past and present

        int counter = 0;
        for (Entities entities : dummyWorld.getEntities().values()) {
            temp[counter][0] = entities.getPopulation();
            counter++;
        }

        counter = 0;
        for (Entities entities : simulation.getWorld().getEntities().values()) {
            temp[counter][1] = entities.getPopulation();
            counter++;
        }

        counter = 0;
        for (Entities entities : simulation.getWorld().getEntities().values()) {
            res.put(entities.getName(), temp[counter]);
            counter++;
        }
        return res;
    }

    public Map<String, Long> GetHistogramEntitiesProperty(String uuid, String entityName, String propertyName) {
        Simulation simulation = historyManager.getRecord(uuid);
        Entities entities = simulation.getWorld().getEntities().get(entityName);
        Map<String, Long> histogram = entities.getEntities().stream()
                .collect(Collectors.groupingBy(e -> e.getProperties().get(propertyName).getValue()
                        , Collectors.counting()));
        return histogram.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    public boolean SaveState(String filePath) {
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this.historyManager);
            return true;
        } catch (IOException e) {
           return false;
        }
    }

    public boolean RestoreState(String filePath){
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(filePath)))) {
            this.historyManager = (HistoryManager) in.readObject();
            this.dummyWorld = historyManager.getOriginalWorld();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }
}
