package api;

import classes.*;
import classes.dto.HistoryManagerDTO;
import classes.dto.PropertyDTO;
import classes.dto.ThreadPoolQueueDTO;
import classes.dto.WorldDTO;
import exceptions.InvalidTypeException;
import helpers.HistoryManager;
import helpers.XmlDeserializer;
import resources.generated.PRDWorld;
import simulations.Simulation;
import validator.Consts;
import validator.Utils;
import validator.Validator;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

public class Engine implements Serializable {
    PRDWorld currentWorld;
    World dummyWorld;
    HistoryManager historyManager;

    ThreadPoolManager threadPoolManager;

    public Engine() {
        threadPoolManager = new ThreadPoolManager();
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
        threadPoolManager.setThreadsAmount(prdWorld.getPRDThreadCount());
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

    public boolean SetEnvPropertyByUuid(String uuid, String name, String value) {
        Property p = historyManager.getRecord(uuid).getWorld().getEnvironmentProperties().get(name);
        if (value == null) {
            p.setRandomValue(0);
            return true;
        } else {
            if (p.validateValue(value)) {
                p.setValue(value, 0);
                return true;
            }
            return false;
        }
    }

    public Response SetPopulationEntityByUuid(String uuid, String name, int value) {
        World world = historyManager.getRecord(uuid).getWorld();
        Entities e = world.getEntities().get(name);
        e.setPopulation(value);

        int maxPopulation = world.getGrid().getColumns() * world.getGrid().getRows();
        int currentPopulation = world.getEntities().values().stream().mapToInt(Entities::getPopulation).sum();
        if (currentPopulation > maxPopulation) {
            return new Response(Response.Status.ERROR,
                    String.format("Setting population failed: An invalid population found (%s entities). Max population is: %s", currentPopulation, maxPopulation));
        }
        e.initEntitiesList();

        return new Response(Response.Status.SUCCESS);
    }

    public String CreateNewSimulation() {
        World world = new World(dummyWorld, true);
        Simulation simulation = new Simulation(world);
        historyManager.addRecord(simulation);
        return simulation.getUuid().toString();
    }

    public void DeleteSimulation(String uuid){
        historyManager.deleteRecord(uuid);
    }

    public void StartSimulation(String uuid) {
        threadPoolManager.executeTask(() -> {
            try {
                Simulation s = historyManager.getRecord(uuid);
                for (Property property : s.getWorld().getEnvironmentProperties().values()) {
                    if (property.getValue() == null)
                        property.setRandomValue(0);
                }
                s.Start();
            } catch (Exception exception) {
                historyManager.getRecord(uuid).Error(exception.getMessage());
            }
        });
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

    public HashMap<String, int[]> GetEntitiesHistogram(String uuid) {
        Simulation simulation = historyManager.getRecord(uuid);
        List<World> history = simulation.getTicksHistory();
        HashMap<String, int[]> res = new HashMap<>();
        simulation.getWorld().getEntities().values().forEach(e -> {
            int[] temp = new int[history.size()];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = history.get(i).getEntities().get(e.getName()).getPopulation();
            }
            res.put(e.getName(), temp);
        });

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

    public double GetConsistencyOfProperty(String uuid, String entityName, String propertyName) {
        Simulation simulation = historyManager.getRecord(uuid);
        Entities entities = simulation.getWorld().getEntities().get(entityName);
        int ticks = simulation.getCurrentTicks();
        return entities.getEntities().stream().mapToDouble(e -> {
            double consistency = e.getProperties().get(propertyName).getConsistency();
            if (consistency == 0) return ticks;
            return ticks / consistency;
        }).average().getAsDouble();
    }

    public double GetAverageOfProperty(String uuid, String entityName, String propertyName) {
        Simulation simulation = historyManager.getRecord(uuid);
        Entities entities = simulation.getWorld().getEntities().get(entityName);
        if (!Consts.RANGE_TYPES.contains(entities.getProperties().get(propertyName).getType())) {
            throw new InvalidTypeException("", "", "");
        }
        return entities.getEntities().stream().mapToDouble(e -> Utils.parseFloat(e.getProperties().get(propertyName).getValue(), "average")).average().getAsDouble();
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

    public boolean RestoreState(String filePath) {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(filePath)))) {
            this.historyManager = (HistoryManager) in.readObject();
            this.dummyWorld = historyManager.getOriginalWorld();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    public Response PauseSimulation(String uuid) {
        if (!historyManager.getRecord(uuid).Pause()) {
            return new Response(Response.Status.ERROR, "Can't pause finished simulation");
        }
        return new Response(Response.Status.SUCCESS);
    }

    public Response ResumeSimulation(String uuid) {
        if (!historyManager.getRecord(uuid).Resume()) {
            return new Response(Response.Status.ERROR, "Can't resume finished simulation");
        }
        return new Response(Response.Status.SUCCESS);
    }

    public void ForwardSimulation(String uuid) {
        historyManager.getRecord(uuid).Forward();
    }

    public void PreviousSimulation(String uuid) {
        historyManager.getRecord(uuid).Previous();
    }

    public void StopSimulation(String uuid) {
        historyManager.getRecord(uuid).Stop();
    }

    public WorldDTO GetSimulationStartWorld(String uuid){
        return new WorldDTO(historyManager.getRecord(uuid).getTicksHistory().getFirst());
    }

    public ThreadPoolQueueDTO GetQueueData(){
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) threadPoolManager.getExecutorService();
        try {
            int waitingTasks = threadPoolExecutor.getQueue().size();

            int activeThreads = threadPoolExecutor.getActiveCount();

            long completedTasks = threadPoolExecutor.getCompletedTaskCount();

            return new ThreadPoolQueueDTO(waitingTasks, activeThreads, completedTasks);
        }
        catch (Exception e){
            return new ThreadPoolQueueDTO(0,0,0);
        }
    }
}
