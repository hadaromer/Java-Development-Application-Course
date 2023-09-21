package controller;

import classes.dto.GridDTO;
import classes.dto.PropertyDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PropertyController extends AbstractController{

    @FXML
    private TextField propertyNameField;

    @FXML
    private TextField propertyTypeField;

    @FXML
    private TextField propertyRangeField;

    @FXML
    private TextField propertyValueField;

    public void init(Object propertyObject) {
        PropertyDTO property = (PropertyDTO) propertyObject;
        propertyNameField.setText(property.getName());
        propertyTypeField.setText(property.getType());
        if (property.isRange()) {
            propertyRangeField.setText(property.getFrom()+" - " +property.getTo());
        }
        else {
            propertyRangeField.setText("No range");
        }
        if(property.isRandomInit()){
            propertyValueField.setText("Random init");

        }
        else {
            propertyValueField.setText(property.getValue());
        }
    }

}
