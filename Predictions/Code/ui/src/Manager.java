import classes.dto.HistoryManagerDTO;
import classes.dto.PropertyDTO;
import classes.dto.SimulationDTO;
import classes.dto.WorldDTO;

import java.io.FileNotFoundException;
import java.util.*;

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

    public void ShowMainMenu() {
        MainMenu.displayMenu();
    }

    public void LoadXmlToEngine(String path) {
        try {
            if (path == null) return;
            engine.LoadXml(path);
            System.out.println("\nSimulation loaded successfully :)\n");
        } catch (FileNotFoundException e) {
            System.out.println("Can't find file. Make sure the path is written well.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void RunSimulation() {
        if (isXmlLoaded()) return;
        String uuid = engine.CreateNewSimulation();
        SetEnvironmentPropertiesForSimulation(uuid);
        String result = engine.StartSimulation(uuid);
        System.out.println("--------------");
        System.out.println("\n Simulation " + uuid + " " + result + "\n");
        System.out.println("--------------");
    }

    private boolean isXmlLoaded() {
        if (!engine.isReady()) {
            System.out.println("\nPlease load xml file first\n");
            return true;
        }
        return false;
    }

    private void SetEnvironmentPropertiesForSimulation(String uuid) {
        System.out.println("\nEnvironment properties for this simulation:\n" +
                "Press enter when you finish\n" +
                "The values that weren't set will randomly set\n");

        ArrayList<PropertyDTO> envProperties = engine.GetEnvProperties();
        int counter = 0;
        for (PropertyDTO property : envProperties) {
            System.out.println("[" + ++counter + "] " + property);
            System.out.println("--------------");
        }
        int choice = 0;
        while (choice != -1) {
            System.out.println("Please choose the property you want to set value to: ");
            choice = UiScanner.getUserChoice(counter);
            if (choice != -1) {
                PropertyDTO property = envProperties.get(choice - 1);
                System.out.println("Setting value for environment property " + property.getName());
                String value = UiScanner.getEnvPropertyValue(property.getType(), property.isRange(),
                        property.getFrom(), property.getTo());
                engine.SetEnvPropertyByUuid(uuid, property.getName(), value);
                System.out.println("Setting value for environment property " + property.getName() + " succeed!");
                System.out.println("\n--------------\n");
            }
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

    public void PrintPastSimulations() {
        if (isXmlLoaded()) return;
        HistoryManagerDTO history = engine.GetPastSimulations();
        List<SimulationDTO> simulations = history.getHistory();
        if (!simulations.isEmpty()) {
            PastSimulationsHandler.PrintPastSimulationByHistory(simulations);
            SimulationDTO simulation = PastSimulationsHandler.ChooseSimulationToExplore(simulations);
            if (simulation != null) {
                PastSimulationsHandler.ResultView resultView = PastSimulationsHandler.ChooseResultsView();
                if (resultView != null) {
                    if (resultView == PastSimulationsHandler.ResultView.COUNT) {
                        HashMap<String, int[]> populationByEntities = engine
                                .GetCountOfEntitiesBeforeAndAfter(simulation.getUuid().toString());
                        PastSimulationsHandler.PrintByCount(populationByEntities);
                    } else {
                        String entity = PastSimulationsHandler.ChooseEntityForHistogram(simulation.getWorld().getEntities());
                        if (entity != null) {
                            String property = PastSimulationsHandler.ChoosePropertyForHistogram(entity,
                                    simulation.getWorld().getEntities().get(0).getProperties());
                            if (property != null) {
                                Map<String, Long> histogram = engine.GetHistogramEntitiesProperty(simulation.getUuid().toString(), entity, property);
                                PastSimulationsHandler.PrintByHistogram(histogram);
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("\nCan't find any simulations. Run one first!");
        }
    }

    public void SaveStateToFile() {
        if (isXmlLoaded()) return;
        String filePath = UiScanner.getValidStateFilePath();
        if (engine.SaveState(filePath + ".ser")) {
            System.out.println("\nSaved successfully :)\n");
        } else {
            System.out.println("\nfailed :(\n");
        }
    }

    public void RestoreFromFile() {
        String filePath = UiScanner.getValidFilePathByExtension("ser");
        if(filePath != null) {
            if (engine.RestoreState(filePath)) {
                System.out.println("\nRestored successfully :)\n");
            } else {
                System.out.println("\nfailed :(\n");
            }
        }
    }
}
