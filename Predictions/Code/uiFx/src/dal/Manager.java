package dal;

import api.Engine;
import api.Response;
import classes.dto.HistoryManagerDTO;
import classes.dto.ThreadPoolQueueDTO;
import classes.dto.WorldDTO;
import exceptions.InvalidMaxPopulationException;
import exceptions.InvalidRangeException;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

public class Manager {
    private Engine engine;

    private static class Loader {
        static final Manager INSTANCE = new Manager();
    }

    private Manager() {
        this.engine = new Engine();
    }

    public static Manager getInstance() {
        return Loader.INSTANCE;
    }

    public Response LoadXmlToEngine(String path) {
        try {
            if (path == null) return new Response(Response.Status.ERROR, "Please provide file");
            engine.LoadXml(path);
            return new Response(Response.Status.SUCCESS);
        } catch (FileNotFoundException e) {
            return new Response(Response.Status.ERROR, "Can't find file. Make sure the path is written well.");
        } catch (Exception e) {
            return new Response(Response.Status.ERROR, e.getMessage());
        }
    }

    public void PrintSimulationDetails() {
        if (isXmlLoaded()) return;
        WorldDTO world = engine.GetSimulationDetails();
        System.out.println("\n--------------");
        System.out.println("SIMULATION DETAILS");
        System.out.println("--------------\n");
        System.out.println("ENTITIES:");
        world.getEntities().forEach(System.out::println);
        System.out.println("RULES:");
        world.getRules().forEach(System.out::println);
        System.out.println("\n\nTERMINATION:");
        System.out.println(world.getTermination());
    }

    public WorldDTO getWorld() {
        if (!isXmlLoaded()) return null;
        return engine.GetSimulationDetails();
    }

    public boolean isXmlLoaded() {
        return engine.isReady();
    }

    public void StartNewSimulation(HashMap<String, Integer> population, HashMap<String, String> envProperties) {
        String uuid = engine.CreateNewSimulation();
        envProperties.forEach((k, v) -> {
            if (v != null) {
                boolean success = engine.SetEnvPropertyByUuid(uuid, k, v);
                if (!success) {
                    engine.DeleteSimulation(uuid);
                    throw new InvalidRangeException(k, "check hover on property type", "Setting environment properties");
                }
            }
        });

        population.forEach((k, v) -> {
            Response res = engine.SetPopulationEntityByUuid(uuid, k, v);
            if (res.getStatus() == Response.Status.ERROR) {
                engine.DeleteSimulation(uuid);
                throw new RuntimeException(res.getMessage());
            }
        });
        engine.StartSimulation(uuid);
    }

    public HistoryManagerDTO GetPastSimulations() {
        return engine.GetPastSimulations();
    }

    public Response PauseSimulation(String uuid) {
        return engine.PauseSimulation(uuid);
    }
    public Response ResumeSimulation(String uuid) {
        return engine.ResumeSimulation(uuid);
    }

    public void ForwardSimulation(String uuid) {
        engine.ForwardSimulation(uuid);
    }

    public void PreviousSimulation(String uuid) {
        engine.PreviousSimulation(uuid);
    }
    public void StopSimulation(String uuid) {
        engine.StopSimulation(uuid);
    }

    public HashMap<String, int[]> GetEntitiesHistogram(String uuid) {
        return engine.GetEntitiesHistogram(uuid);
    }

    public Map<String, Long> GetHistogramEntitiesProperty(String uuid, String entityName, String propertyName) {
        return engine.GetHistogramEntitiesProperty(uuid, entityName, propertyName);
    }

    public double GetConsistencyOfProperty(String uuid, String entityName, String propertyName) {
        return engine.GetConsistencyOfProperty(uuid, entityName, propertyName);
    }

    public double GetAverageOfProperty(String uuid, String entityName, String propertyName) {
        return engine.GetAverageOfProperty(uuid, entityName, propertyName);
    }

    public WorldDTO GetSimulationStartWorld(String uuid){
        return engine.GetSimulationStartWorld(uuid);
    }

    public ThreadPoolQueueDTO GetQueueData(){
       return engine.GetQueueData();
    }
}
