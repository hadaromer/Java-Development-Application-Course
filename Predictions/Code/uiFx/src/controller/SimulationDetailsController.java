package controller;

import classes.dto.WorldDTO;
import dal.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.SimulationDetailsTreeModel;
import model.SimulationTreeItem;
import utils.enums;
import utils.utilsFunctions;

import java.net.URL;

public class SimulationDetailsController {
    private static final String GRID_FXML = "/view/GridTerminationView.fxml";
    private static final String PROPERTY_FXML = "/view/PropertyView.fxml";
    private static final String ACTION_FXML = "/view/ActionView.fxml";
    private static final String RULE_FXML = "/view/RuleView.fxml";


    private AppController mainController;
    @FXML
    private VBox detailsPane;
    @FXML
    private TreeView<String> SimulationDetailsTree;
    private WorldDTO worldDTO;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setTreeView() {
        worldDTO = Manager.getInstance().getWorld();
        SimulationDetailsTree.setRoot(new SimulationTreeItem("Simulation", enums.DetailType.TITLE));
        SimulationDetailsTree.getRoot().getChildren().add(new SimulationTreeItem("Grid", enums.DetailType.GRID));
        SimulationDetailsTree.getRoot().getChildren().add(SimulationDetailsTreeModel.getEnvironmentPropertiesRoot(worldDTO.getEnvironmentProperties()));
        SimulationDetailsTree.getRoot().getChildren().add(SimulationDetailsTreeModel.getEntitiesRoot(worldDTO.getEntities()));
        SimulationDetailsTree.getRoot().getChildren().add(SimulationDetailsTreeModel.getRulesRoot(worldDTO.getRules()));
        SimulationDetailsTree.getRoot().getChildren().add(new SimulationTreeItem("Termination", enums.DetailType.TERMINATION));

        SimulationDetailsTree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                SimulationTreeItem selectedItem = (SimulationTreeItem) SimulationDetailsTree.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    switch (selectedItem.getDetailType()) {
                        case GRID:
                            showDetailsOfChild(GRID_FXML, worldDTO.getGrid());
                            break;
                        case PROPERTY:
                            showDetailsOfChild(PROPERTY_FXML, selectedItem.getData());
                            break;
                        case ACTION:
                            showDetailsOfChild(ACTION_FXML, selectedItem.getData());
                            break;
                        case RULE:
                            showDetailsOfChild(RULE_FXML,selectedItem.getData());
                            break;
                        case TERMINATION:
                            showDetailsOfChild(GRID_FXML, worldDTO.getTermination());
                            break;
                        default:
                            return;
                    }
                }
            }
        });
    }

    public void showDetailsOfChild(String fxmlUrl, Object data){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(fxmlUrl);
            fxmlLoader.setLocation(url);
            Node root = fxmlLoader.load(url.openStream());
            AbstractController controller = fxmlLoader.getController();
            controller.init(data);
            detailsPane.getChildren().setAll(root);
            VBox.setVgrow(root, Priority.ALWAYS);
            utilsFunctions.fadeAnimation(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
