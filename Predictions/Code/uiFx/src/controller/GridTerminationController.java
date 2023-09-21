package controller;

import classes.dto.GridDTO;
import classes.dto.TerminationDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class GridTerminationController extends AbstractController {

    @FXML
    private StackPane stackpaneGlobe;

    @FXML
    private Label columnLabel;

    @FXML
    private Label rowLabel;

    public void init(Object object){
        if(object instanceof GridDTO) {
            stackpaneGlobe.getStyleClass().setAll("stackpaneGlobe");
            GridDTO grid = (GridDTO) object;
            rowLabel.setText(grid.getRows() + "\nrows");
            rowLabel.setVisible(true);
            columnLabel.setText(grid.getColumns() + "\ncolumns");
        }
        else{
            stackpaneGlobe.getStyleClass().setAll("stackpaneGlobeDestroyed");
            TerminationDTO termination = (TerminationDTO) object;
            if(termination.isByUser()){
                columnLabel.setText("Termination is by user");
            }
            else{
                columnLabel.setText("Termination by ticks: "+ (termination.getTicks()!=-1 ? termination.getTicks() : "NOT SET")
                + "Termination by seconds: " + (termination.getSeconds()!=-1 ? termination.getSeconds() : "NOT SET"));
            }
            rowLabel.setText(null);
            rowLabel.setVisible(false);
        }
    }


}
