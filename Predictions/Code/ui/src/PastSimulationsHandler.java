import classes.dto.EntitiesDTO;
import classes.dto.PropertyDTO;
import classes.dto.SimulationDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PastSimulationsHandler {
    public enum ResultView {COUNT, HISTOGRAM}

    ;
    private static final int NUM_RESULT_VIEW = 2;

    public static void PrintPastSimulationByHistory(List<SimulationDTO> simulations) {
        System.out.println("\n--------------");
        System.out.println("PAST SIMULATIONS");
        System.out.println("--------------\n");
        int counter = 0;
        for (SimulationDTO simulation : simulations) {
            System.out.println("[" + ++counter + "] " + simulation);
            System.out.println("--------------");
        }
    }

    public static SimulationDTO ChooseSimulationToExplore(List<SimulationDTO> simulations) {
        int maxValue = simulations.size();
        int choice = 0;
        while (choice != -1) {
            System.out.println("\nPlease choose the simulation you want to explore: ");
            choice = UiScanner.getUserChoice(maxValue);
            if (choice != -1) {
                return simulations.get(choice - 1);
            }
        }
        return null;
    }

    public static ResultView ChooseResultsView() {
        int choice = 0;
        while (choice != -1) {
            System.out.println("\nPlease choose from the options below:");
            System.out.println("\n[1] Entities amount before and after simulation");
            System.out.println("[2] Property histogram");
            choice = UiScanner.getUserChoice(NUM_RESULT_VIEW);
            if (choice != -1) {
                return ResultView.values()[choice - 1];
            }
        }
        return null;
    }

    public static void PrintByCount(HashMap<String, int[]> populationByEntities) {
        int i = 0;
        for (String entities : populationByEntities.keySet()) {
            System.out.println("\n" + entities + " before:" + populationByEntities.get(entities)[0] + " after:" +
                    populationByEntities.get(entities)[1] + "\n");
            i++;
        }
    }

    public static String ChooseEntityForHistogram(List<EntitiesDTO> entities) {
        System.out.println("\nPossible entities: \n");
        for (int i = 1; i < entities.size() + 1; i++) {
            System.out.println("[" + i + "] " + entities.get(i - 1).getName() + "\n");
        }
        int maxValue = entities.size();
        int choice = 0;
        while (choice != -1) {
            System.out.println("\nPlease choose the entity you want to explore: ");
            choice = UiScanner.getUserChoice(maxValue);
            if (choice != -1) {
                return entities.get(choice - 1).getName();
            }
        }
        return null;
    }

    public static String ChoosePropertyForHistogram(String entityName, List<PropertyDTO> properties) {
        System.out.println("\nPossible properties for " + entityName + ": \n");
        for (int i = 1; i < properties.size() + 1; i++) {
            System.out.println("[" + i + "] " + properties.get(i - 1).getName() + "\n");
        }
        int maxValue = properties.size();
        int choice = 0;
        while (choice != -1) {
            System.out.println("\nPlease choose the property you want to get histogram: ");
            choice = UiScanner.getUserChoice(maxValue);
            if (choice != -1) {
                return properties.get(choice - 1).getName();
            }
        }
        return null;
    }

    public static void PrintByHistogram(Map<String, Long> histogram) {
        if (histogram.isEmpty()) {
            System.out.println("\nAll entities died. Nothing to show.\n");
        } else {
            for (String value : histogram.keySet()) {
                System.out.println("- Value: " + value + " Count: " + histogram.get(value) + "\n");
            }
        }
    }
}
