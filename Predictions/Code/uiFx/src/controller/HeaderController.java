package controller;

import classes.dto.ThreadPoolQueueDTO;
import dal.Manager;
import api.Response;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import utils.utilsFunctions;

import java.io.File;

public class HeaderController {

    @FXML
    private TextField XMLFilePathText;

    @FXML
    private Button detailsButton;
    @FXML
    private Button newExecutionButton;
    @FXML
    private Button resultsButton;
    @FXML
    private Button queueButton;
    @FXML
    private ComboBox themesComboBox;
    @FXML
    private CheckBox animationsCheckBox;
    private AppController mainController;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void init() {
        animationsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> utilsFunctions.animated = newValue);

        ObservableList<String> themes = FXCollections.observableArrayList("Default", "Barbie World", "Alien World");
        themesComboBox.setItems(themes);
        themesComboBox.setPromptText("Select a world");
        themesComboBox.setOnAction(event -> {
            if (themesComboBox.getValue() != null) {
                switch (themesComboBox.getValue().toString()) {
                    case "Default":
                        themesComboBox.getScene().getStylesheets().setAll(getClass().getResource("/css/default.css").toExternalForm());
                        break;
                    case "Barbie World":
                        themesComboBox.getScene().getStylesheets().setAll(getClass().getResource("/css/barbieWorld.css").toExternalForm());
                        break;
                    case "Alien World":
                        themesComboBox.getScene().getStylesheets().setAll(getClass().getResource("/css/alienWorld.css").toExternalForm());
                        break;
                    default:
                        return;
                }
            }
        });
    }

    @FXML
    void LoadSimulationHandler(MouseEvent event) {
        ThreadPoolQueueDTO threadPoolQueue = Manager.getInstance().GetQueueData();
        if ((threadPoolQueue.getWaitingTasks() > 0 || threadPoolQueue.getActiveThreads() > 0)) {
            boolean ans = utilsFunctions.showConfirm("Are you sure you want to continue? There are waiting or running simulations.");
            if (!ans) return;

        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Simulation file", "*.xml"));
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            Response res = Manager.getInstance().LoadXmlToEngine(file.getPath());
            if (res.getStatus() == Response.Status.SUCCESS) {
                XMLFilePathText.setText(file.getPath());
                detailsButton.setDisable(false);
                newExecutionButton.setDisable(false);
                queueButton.setDisable(false);
                mainController.simulationLoaded();
            } else {
                utilsFunctions.showAlert(res.getMessage());
            }
        }
    }

    public void switchToNewExecutionScreen() {
        mainController.switchToNewExecutionScreen();
    }

    public void switchToDetailsScreen() {
        mainController.switchToDetailsScreen();
    }

    public void setResultsButtonDisable(boolean disable) {
        resultsButton.setDisable(disable);
    }

    public void switchToResultsScreen() {
        mainController.switchToResultsScreen();
    }

    @FXML
    void queueOnClick(MouseEvent event) {
        ThreadPoolQueueDTO threadPoolQueue = Manager.getInstance().GetQueueData();
        utilsFunctions.showMessage("waiting: " + threadPoolQueue.getWaitingTasks() +
                "\n active: " + threadPoolQueue.getActiveThreads() +
                "\n completed: " + threadPoolQueue.getCompletedTasks());
    }

    @FXML
    void helpOnClick(MouseEvent event) {
        mainController.helpClicked();
    }

}
