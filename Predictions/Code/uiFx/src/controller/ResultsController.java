package controller;

import api.Response;
import classes.dto.EntitiesDTO;
import classes.dto.GridDTO;
import classes.dto.PropertyDTO;
import classes.dto.SimulationDTO;
import dal.Manager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utils.utilsFunctions;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static classes.dto.SimulationDTO.State;

public class ResultsController {
    private AppController mainController;

    @FXML
    private TableView<SimulationDTO> simulationsTableView;

    @FXML
    private TableColumn<SimulationDTO, String> uuidColumn;

    @FXML
    private TableColumn<SimulationDTO, String> createdColumn;

    @FXML
    private TableColumn<SimulationDTO, String> timeColumn;

    @FXML
    private TableColumn<SimulationDTO, String> ticksColumn;

    @FXML
    private TableColumn<SimulationDTO, String> stateColumn;

    @FXML
    private TableView<EntitiesDTO> entityPopulationTableView;

    @FXML
    private TableColumn<EntitiesDTO, String> entityNameColumn;

    @FXML
    private TableColumn<EntitiesDTO, String> populationColumn;

    @FXML
    private Button rerunButton;

    @FXML
    private Button prevButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button pauseResumeButton;

    @FXML
    private Button forwButton;
    @FXML
    private Label errorLabel;

    @FXML
    private GridPane entitiesLocationsGrid;

    @FXML
    private LineChart<?, ?> entitiesHistogramLineChart;

    @FXML
    private ComboBox<String> entityComboBox;

    @FXML
    private ComboBox<String> propertyComboBox;

    @FXML
    private PieChart propertyHistogramPieChart;

    @FXML
    private Label consistencyLabel;

    @FXML
    private Label avgLabel;
    @FXML
    private Tab entitiesHistogramTab;

    @FXML
    private Tab propertiesTab;
    @FXML
    private Tab worldTab;
    @FXML
    private TabPane resultTab;
    private List<SimulationDTO> simulations;
    private ObservableList<SimulationDTO> data = FXCollections.observableArrayList();
    private SimulationDTO selectedSimulation;
    private UUID selectedUuid;
    private ObservableList<EntitiesDTO> entities = FXCollections.observableArrayList();
    private Rectangle[][] rectangleCells;
    private HashMap<String, Color> entityColors;
    private Color[] colors = {Color.GREEN,Color.RED,Color.BLUE,Color.YELLOW,Color.ORANGE,Color.BISQUE,Color.GOLD};
    private int rows;
    private int cols;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void init() {
        simulations = Manager.getInstance().GetPastSimulations().getHistory();
        data.setAll(simulations);
        simulationsTableView.setItems(data);

        initEntitiesLocations();
        initColors();
        initPastSimulations();
        initEntitiesPopulation();
        enableRefreshResults();
    }

    private void initColors() {
        entityColors = new HashMap<>();
        AtomicInteger i= new AtomicInteger();
        data.get(0).getWorld().getEntities().forEach(entity -> {
            entityColors.put(entity.getName(),colors[i.getAndIncrement()]);
        });
    }

    private void initEntitiesPopulation() {
        entityNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        populationColumn.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getPopulation())));
    }

    private void initPastSimulations() {
        uuidColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUuid().toString()));
        createdColumn.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getStartDate())));
        timeColumn.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getCurrentTime())));
        ticksColumn.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getCurrentTicks())));
        stateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getState().toString()));

        simulationsTableView.setRowFactory(tv -> {
            TableRow<SimulationDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    selectedSimulation = row.getItem();
                    selectedUuid = row.getItem().getUuid();
                    entities.setAll(row.getItem().getWorld().getEntities());
                    handleSimulationSelected();
                }
            });
            return row;
        });
    }

    private void enableRefreshResults() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = this::refreshResults;
        scheduler.scheduleAtFixedRate(task, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void refreshResults() {
        try {
            List<SimulationDTO> updatedSimulations = Manager.getInstance().GetPastSimulations().getHistory();
            updatedSimulations.forEach(s -> {
                if (data.stream().anyMatch(o -> o.getUuid().equals(s.getUuid()))) {
                    if (s.getUuid().equals(selectedUuid)) {
                        if (selectedSimulation == null || !selectedSimulation.isSameState(s)) {
                            entities.setAll(s.getWorld().getEntities());
                            entityPopulationTableView.refresh();
                            String[][] locations = s.getWorld().getEntitiesLocations();
                            updateEntitiesLocations(locations);
                            if (s.getState() == State.FINISHED_BY_TICKS ||
                                    s.getState() == State.FINISHED_BY_TIME ||
                                    s.getState() == State.STOPPED || s.getState() == State.ERROR) {
                                Platform.runLater(this::handleFinishedSimulation);
                            }
                        }
                    }
                    data.stream().filter(o -> o.getUuid().equals(s.getUuid())).findFirst().get().updateSimulation(s);
                } else {
                    data.add(s);
                }
            });
            simulationsTableView.refresh();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void initEntitiesLocations() {
        entitiesLocationsGrid.getChildren().removeIf(node -> node instanceof Rectangle);
        GridDTO grid = simulations.get(0).getWorld().getGrid();
        rows = grid.getRows();
        cols = grid.getColumns();
        rectangleCells = new Rectangle[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                rectangleCells[row][col] = new Rectangle(7, 7, new Color(0, 0, 0, 0));
                entitiesLocationsGrid.add(rectangleCells[row][col], col, row);
            }
        }
        entitiesLocationsGrid.setGridLinesVisible(true);
        RowConstraints con = new RowConstraints();
        con.setPrefHeight(10);
        con.setVgrow(Priority.NEVER);
        entitiesLocationsGrid.getRowConstraints().setAll(con);
    }

    private void updateEntitiesLocations(String[][] locations) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (locations[row][col] != null) {
                        rectangleCells[row][col].setFill(entityColors.get(locations[row][col]));
                } else {
                    rectangleCells[row][col].setFill(Color.TRANSPARENT);
                }
            }
        }
    }

    public void clearGraphs() {
        entitiesHistogramLineChart.getData().clear();
        propertyHistogramPieChart.getData().clear();
        entityComboBox.getItems().clear();
        entityComboBox.getSelectionModel().clearSelection();
        propertyComboBox.getItems().clear();
        propertyComboBox.getSelectionModel().clearSelection();
        consistencyLabel.setText("");
        consistencyLabel.setVisible(false);
        avgLabel.setText("");
        avgLabel.setVisible(false);
    }

    private void buildEntitiesHistogram() {
        entitiesHistogramLineChart.getData().clear();
        HashMap<String, int[]> res = Manager.getInstance().GetEntitiesHistogram(selectedUuid.toString());
        res.forEach((k, v) -> {
            XYChart.Series series = new XYChart.Series();
            series.setName(k);
            for (int i = 0; i < v.length; i++) {
                series.getData().add(new XYChart.Data(String.valueOf(i), v[i]));
            }
            entitiesHistogramLineChart.getData().add(series);
        });

    }

    private void buildEntityPropertyHistogram() {
        ObservableList<String> entitiesNames = FXCollections.observableArrayList(entities.stream().filter(e->e.getPopulation()>0)
                .map(EntitiesDTO::getName).collect(Collectors.toList()));

        entityComboBox.setItems(entitiesNames);
        entityComboBox.setPromptText("Select an entity to explore");
        entityComboBox.setOnAction(event -> {
            if (entityComboBox.getValue() != null) {
                propertyComboBox.getSelectionModel().clearSelection();
                propertyHistogramPieChart.getData().clear();
                consistencyLabel.setText("");
                consistencyLabel.setVisible(false);
                avgLabel.setText("");
                avgLabel.setVisible(false);
                ObservableList<String> properties = FXCollections.observableArrayList(entities.stream()
                        .filter(e->e.getName().equals(entityComboBox.getValue())).findFirst().get().getProperties().
                        stream().map(PropertyDTO::getName).collect(Collectors.toList()));
                propertyComboBox.setItems(properties);
            }
        });

        propertyComboBox.setPromptText("Select a property to explore");
        propertyComboBox.setOnAction(event -> {
            if (propertyComboBox.getValue() != null) {
                buildPieChart();
                setConsistencyLabel();
                setAvgLabel();
            }
        });
    }

    public void buildPieChart() {
        propertyHistogramPieChart.getData().clear();
        Map<String, Long> values = Manager.getInstance().GetHistogramEntitiesProperty(selectedUuid.toString()
                , entityComboBox.getValue(), propertyComboBox.getValue());
        values.forEach((k, v) -> {
            PieChart.Data slice = new PieChart.Data(k, v);
            propertyHistogramPieChart.getData().add(slice);
        });
    }

    public void setConsistencyLabel() {
        consistencyLabel.setVisible(true);
        double consistency = Manager.getInstance().GetConsistencyOfProperty(selectedUuid.toString()
                , entityComboBox.getValue(), propertyComboBox.getValue());
        consistencyLabel.setText("Consistency: " + String.format("%.3f", consistency));
    }

    public void setAvgLabel() {
        try {
            avgLabel.setVisible(true);
            double avg = Manager.getInstance().GetAverageOfProperty(selectedUuid.toString()
                    , entityComboBox.getValue(), propertyComboBox.getValue());
            avgLabel.setText("Average value: " + String.format("%.3f", avg));
        } catch (Exception e) {
            avgLabel.setText("Average not available - not a numeric property");
        }
    }

    private void handleSimulationSelected() {
        utilsFunctions.visibleNodes(true, entityPopulationTableView, prevButton, forwButton, stopButton, pauseResumeButton, rerunButton);
        entityPopulationTableView.setItems(entities);
        resultTab.getSelectionModel().select(worldTab);

        switch (selectedSimulation.getState()) {
            case STOPPED:
            case FINISHED_BY_TICKS:
            case FINISHED_BY_TIME:
            case ERROR:
                handleFinishedSimulation();
                break;
            default:
                handleRunningSimulation();
                break;
        }
    }

    private void handleRunningSimulation() {
        entityPopulationTableView.setVisible(true);
        errorLabel.setVisible(false);
        utilsFunctions.disableNodes(false, prevButton, pauseResumeButton, stopButton, forwButton);
        utilsFunctions.disableNodes(true, rerunButton);
        entitiesHistogramTab.setDisable(true);
        propertiesTab.setDisable(true);
        clearGraphs();
    }

    private void handleFinishedSimulation() {
        utilsFunctions.disableNodes(true, prevButton, pauseResumeButton, stopButton, forwButton);
        utilsFunctions.disableNodes(false, rerunButton);
        if(selectedSimulation.getState()!=State.ERROR) {
            entityPopulationTableView.setVisible(true);
            entitiesHistogramTab.setDisable(false);
            propertiesTab.setDisable(false);
            buildEntitiesHistogram();
            buildEntityPropertyHistogram();
        }
        else{
            entityPopulationTableView.setVisible(false);
            errorLabel.setVisible(true);
            errorLabel.setText("ERROR: " + selectedSimulation.getErrorMessage());
        }
    }

    @FXML
    void pauseResumeSimulationOnClick(MouseEvent event) {
        if (pauseResumeButton.textProperty().getValue().equals("Pause")) {
            Response res = Manager.getInstance().PauseSimulation(selectedUuid.toString());
            if (res.getStatus() == Response.Status.SUCCESS) {
                pauseResumeButton.setText("Resume");
            } else {
                utilsFunctions.showAlert(res.getMessage());
            }
        } else {
            Response res = Manager.getInstance().ResumeSimulation(selectedUuid.toString());
            if (res.getStatus() == Response.Status.SUCCESS) {
                pauseResumeButton.setText("Pause");
            } else {
                utilsFunctions.showAlert(res.getMessage());
            }

        }
    }

    @FXML
    void forwSimulationOnClick(MouseEvent event) {
        Manager.getInstance().ForwardSimulation(selectedUuid.toString());
    }

    @FXML
    void prevSimulationOnClick(MouseEvent event) {
        Manager.getInstance().PreviousSimulation(selectedUuid.toString());
    }

    @FXML
    void stopOnClick(MouseEvent event) {
        Manager.getInstance().StopSimulation(selectedUuid.toString());
        utilsFunctions.showMessage("Simulation is stopped. You can explore simulation statistics now.");
        handleFinishedSimulation();
    }

    @FXML
    void rerunOnClick(MouseEvent event) {
        mainController.switchToNewExecutionScreen(Manager.getInstance().GetSimulationStartWorld(selectedUuid.toString()));
    }

}
