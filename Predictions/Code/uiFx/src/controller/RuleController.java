package controller;

import classes.dto.ActionDTO;
import classes.dto.RuleDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.concurrent.atomic.AtomicInteger;

public class RuleController extends AbstractController{

    @FXML
    private TextField ticksField;

    @FXML
    private TextField probabilityField;

    @FXML
    private Label ruleLabel;

    public void init(Object ruleObject){
        RuleDTO rule = (RuleDTO) ruleObject;
        ruleLabel.setText(rule.getName());
        ticksField.setText(String.valueOf(rule.getTicks()));
        probabilityField.setText(String.valueOf(rule.getProbability()));
    }

}

