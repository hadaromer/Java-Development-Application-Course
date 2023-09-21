package controller;

import classes.dto.WorldDTO;
import com.sun.xml.internal.ws.api.pipe.Engine;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.*;
import utils.utilsFunctions;

import java.util.HashMap;

public class AppController {

    private HashMap<String,String> HELP_MESSAGES = new HashMap<String, String>() {{
        put("SimulationDetailsComponent", "In order to start your journey please load a valid XML simulation." +
                "\nThen you will be able to view the simulation details and run it!" +
                "\nYou can view how much simulations are running waiting or done by clicking on the queue management button.");
        put("NewExecutionComponent", "Please fill the desired population for each entity." +
                "\nAlso, you can provide custom environment properties when checking the box near the property." +
                "\nFor ranged types just hover the label to view the range.");
        put("ResultsComponent","Yay! Now you can view the results of your simulations!" +
                "\nClick on the simulation you want to view in the table." +
                "\nNow you will see the world and the entities moving around there." +
                "\nYou can pause a simulation and forward or backward ticks manually." +
                "\nWhen a simulation is finished you will be able to explore the simulation statistics.");
    }};


    @FXML
    private BorderPane mainScreenBorderPane;

    @FXML
    private GridPane HeaderComponent;
    @FXML
    private HeaderController HeaderComponentController;
    @FXML
    private HBox SimulationDetailsComponent;
    @FXML
    private SimulationDetailsController SimulationDetailsComponentController;
    @FXML
    public VBox NewExecutionComponent;
    @FXML
    private NewExecutionController NewExecutionComponentController;

    @FXML
    public VBox ResultsComponent;
    @FXML
    private ResultsController ResultsComponentController;
    private Node currentScreen;

    private boolean resultsInited = false;

    @FXML
    public void initialize() {
        if (HeaderComponentController != null && SimulationDetailsComponentController != null
                && NewExecutionComponent != null && ResultsComponent != null) {
            HeaderComponentController.setMainController(this);
            SimulationDetailsComponentController.setMainController(this);
            NewExecutionComponentController.setMainController(this);
            ResultsComponentController.setMainController(this);
            currentScreen = SimulationDetailsComponent;
            HeaderComponentController.init();
        }
    }


    public void simulationLoaded() {
        switchToDetailsScreen();
        resultsInited=false;
        HeaderComponentController.setResultsButtonDisable(true);
        SimulationDetailsComponentController.setTreeView();
        NewExecutionComponentController.init();
    }
    public void switchToDetailsScreen() {
        if(currentScreen!=SimulationDetailsComponent) {
            utilsFunctions.carouselAnimation(currentScreen, SimulationDetailsComponent,true);
            currentScreen = SimulationDetailsComponent;
        }
    }

    public void switchToNewExecutionScreen() {
        if(currentScreen!=NewExecutionComponent) {
            boolean reverse = currentScreen == ResultsComponent;
            utilsFunctions.carouselAnimation(currentScreen, NewExecutionComponent,reverse);
            currentScreen = NewExecutionComponent;
        }
    }

    public void switchToResultsScreen() {
        if(currentScreen!=ResultsComponent) {
            utilsFunctions.carouselAnimation(currentScreen, ResultsComponent,false);
            currentScreen = ResultsComponent;
        }
        if(!resultsInited){
            ResultsComponentController.init();
            HeaderComponentController.setResultsButtonDisable(false);
            resultsInited = true;
        }
    }


    public void switchToNewExecutionScreen(WorldDTO worldDTO) {
        switchToNewExecutionScreen();
        NewExecutionComponentController.init(worldDTO);
    }

    public void helpClicked() {
        utilsFunctions.showMessage(HELP_MESSAGES.get(currentScreen.getId()));
    }
}
