package controller;

import classes.dto.WorldDTO;
import dal.Manager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import utils.textFormatters;
import utils.utilsFunctions;
import validator.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class NewExecutionController {
    private AppController mainController;

    @FXML
    private GridPane entitiesPopulationGrid;

    @FXML
    private GridPane envPropsGrid;

    private WorldDTO world;

    private HashMap<String,Integer> populationValues;
    private HashMap<String,String> envPropsValues;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void init() {
        envPropsGrid.getChildren().clear();
        entitiesPopulationGrid.getChildren().clear();
        world = Manager.getInstance().getWorld();
        SetEntitiesPopulationForm();
        SetEnvsPropsForm();
    }

    public void init(WorldDTO worldToInit) {
        envPropsGrid.getChildren().clear();
        entitiesPopulationGrid.getChildren().clear();
        world = worldToInit;
        SetEntitiesPopulationForm();
        SetEnvsPropsForm();
    }

    private void SetEntitiesPopulationForm() {
        populationValues = new HashMap<>();
        final AtomicInteger counter = new AtomicInteger();
        counter.set(0);
        world.getEntities().forEach(e -> {
            populationValues.put(e.getName(),e.getPopulation());
            final int index = counter.getAndIncrement();
            entitiesPopulationGrid.add(new Label(e.getName()), 0, index);
            TextField temp = new TextField();
            temp.setPromptText("0");
            if(e.getPopulation()>0)
                temp.textProperty().set(String.valueOf(e.getPopulation()));
            temp.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.isEmpty()) {
                        populationValues.put(e.getName(),Utils.parseDecimal(newValue, ""));
                    } else {
                        populationValues.put(e.getName(),0);

                    }
                }
            });
            temp.setTextFormatter(textFormatters.getTextFormatterByType("decimal"));
            entitiesPopulationGrid.add(temp, 1, index);

        });
        AddGridConstraints(counter.get(), entitiesPopulationGrid);
    }

    private void SetEnvsPropsForm() {
        envPropsValues = new HashMap<>();
        final AtomicInteger counter = new AtomicInteger();
        counter.set(0);
        world.getEnvironmentProperties().forEach(e -> {
            envPropsValues.put(e.getName(),e.getValue());
            final int index = counter.getAndIncrement();

            Label label = new Label(e.getType());
            label.getStyleClass().setAll(e.getType() + "-label");
            envPropsGrid.add(label, 0, index);
            if(e.isRange()) {
                label.setText("ranged "+label.getText());
                Tooltip tooltip = new Tooltip("range: "+ e.getFrom() + "-" + e.getTo());
                Tooltip.install(label, tooltip);
            }

            CheckBox checkBox = new CheckBox(e.getName());
            envPropsGrid.add(checkBox, 1, index);

            if(e.getType().equals("boolean")){
                CheckBox temp = new CheckBox();
                temp.setDisable(true);
                if(e.getValue()!=null) {
                    temp.setDisable(false);
                    temp.setSelected(e.getValue().equals("true"));
                    checkBox.setSelected(true);
                }
                temp.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (newValue) {
                            envPropsValues.put(e.getName(), "true");

                        } else {
                            envPropsValues.put(e.getName(),"false");

                            System.out.println("ToggleButton is unchecked.");
                        }
                    }
                });
                envPropsGrid.add(temp, 2, index);
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (newValue) {
                            temp.setDisable(false);
                            envPropsValues.put(e.getName(), temp.textProperty().getValue());
                            System.out.println("Feature is enabled.");
                        } else {
                            temp.setDisable(true);
                            envPropsValues.put(e.getName(),null);
                            System.out.println("Feature is disabled.");
                        }
                    }
                });

            }
            else {
                TextField temp = new TextField();
                temp.setPromptText("Enter value");
                temp.setDisable(true);
                if(e.getValue()!=null) {
                    checkBox.setSelected(true);
                    temp.textProperty().set(e.getValue());
                    temp.setDisable(false);
                }
                temp.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        envPropsValues.put(e.getName(),newValue);
                    }
                });
                temp.setTextFormatter(textFormatters.getTextFormatterByType(e.getType()));
                envPropsGrid.add(temp, 2, index);
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (newValue) {
                            temp.setDisable(false);
                            envPropsValues.put(e.getName(), temp.textProperty().getValue());
                        } else {
                            temp.setDisable(true);
                            envPropsValues.put(e.getName(), null);
                        }
                    }
                });
            }


        });
        AddGridConstraints(counter.get(), envPropsGrid);
    }

    private void AddGridConstraints(int count, GridPane gridPane) {
        gridPane.getRowConstraints().clear();
        for(int i=0; i<count; i++) {
            RowConstraints con = new RowConstraints();
            con.setPrefHeight(70);
            con.setVgrow(Priority.NEVER);
            con.setFillHeight(false);
            gridPane.getRowConstraints().add(con);
        }
    }

    @FXML
    void clearOnClick(MouseEvent event) {
        for (HashMap.Entry<String, Integer> entry : populationValues.entrySet()) {
            entry.setValue(0);
        }
        for (Node node : entitiesPopulationGrid.getChildren()) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                textField.textProperty().set("");
            }
        }

        for (HashMap.Entry<String, String> entry : envPropsValues.entrySet()) {
            entry.setValue(null);
        }

        for (Node node : envPropsGrid.getChildren()) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                textField.textProperty().set("");
                textField.setDisable(true);
            }
            if(node instanceof CheckBox){
                CheckBox checkBox = (CheckBox) node;
                checkBox.setSelected(false);
                if(checkBox.getText().isEmpty()) {
                    checkBox.setDisable(true);
                }
            }
        }
    }

    @FXML
    void StartNewSimulation(MouseEvent event) {
        try {
            Manager.getInstance().StartNewSimulation(populationValues, envPropsValues);
            clearOnClick(null);
            mainController.switchToResultsScreen();
        }
        catch (Exception e){
            utilsFunctions.showAlert(e.getMessage());
        }
    }

}
