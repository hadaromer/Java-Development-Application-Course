import helpers.RandomCreator;

public class MainMenu {
    private static final String[] jokes = {
            "What do ghosts use to predict the future?\n" +
                    "Horror-scopes!",
            "What do you call a table that knows the future?\n" +
                    "A predictable!",
            "Where do meteorologists save their wheather predictions?\n" +
                    "In the cloud.",
            "I never make predictions.\n" +
                    "I never have and I never will."
    };

    enum MenuOption {
        LOAD_XML("Load new XML file"),
        SIMULATION_DETAILS("Simulation details"),
        RUN("Run simulation"),
        PAST_RUNS("Past runs"),
        SAVE_STATE("Save application state"),
        RESTORE_STATE("Restore application state"),

        EXIT("Exit");

        private final String description;

        MenuOption(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public static void displayMenu() {
        System.out.println("Welcome!\n" +
                "Please follow the instructions\n" +
                "Note that pressing enter (empty) will take you back to menu\n" +
                "and in some cases will affect your choice.\n");

        int choice = -1;
        do {
            System.out.println("Menu:");
            for (MenuOption option : MenuOption.values()) {
                System.out.println(option.ordinal() + 1 + " - " + option.getDescription());
            }
            choice = UiScanner.getUserChoice(MenuOption.values().length);
            if (choice != -1) {
                handleUserChoice(choice);
            }
        } while (choice != MenuOption.EXIT.ordinal() + 1);
    }

    private static void handleUserChoice(int choice) {
        MenuOption selectedOption = MenuOption.values()[choice - 1];
        switch (selectedOption) {
            case LOAD_XML:
                Manager.getInstance().LoadXmlToEngine(UiScanner.getValidFilePathByExtension("xml"));
                break;
            case SIMULATION_DETAILS:
                Manager.getInstance().PrintSimulationDetails();
                break;
            case RUN:
                Manager.getInstance().RunSimulation();
                break;
            case PAST_RUNS:
                Manager.getInstance().PrintPastSimulations();
                break;
            case SAVE_STATE:
                Manager.getInstance().SaveStateToFile();
                break;
            case RESTORE_STATE:
                Manager.getInstance().RestoreFromFile();
                break;
            case EXIT:
                System.out.println("Thank you! See you next time :)\n");
                System.out.println(jokes[RandomCreator.getInt(0, 3)]);
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }
}
