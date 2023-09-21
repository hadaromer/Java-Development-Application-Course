package controller;

        import classes.dto.ActionDTO;
        import javafx.fxml.FXML;
        import javafx.scene.control.Label;
        import javafx.scene.control.TextField;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.scene.layout.GridPane;
        import javafx.scene.layout.Priority;
        import javafx.scene.layout.RowConstraints;

        import java.util.concurrent.atomic.AtomicInteger;

public class ActionController extends AbstractController{
    @FXML
    private GridPane actionGridPane;

    @FXML
    private TextField entityField;

    @FXML
    private TextField secondEntityField;

    @FXML
    private TextField propertyField;

    @FXML
    private TextField byField;

    @FXML
    private ImageView secondEntityImg;

    @FXML
    private ImageView actionIcon;

    @FXML
    private Label actionTitle;

    public void init(Object actionObject){
        ActionDTO action = (ActionDTO) actionObject;
        Image actionImage = new Image("resources/"+action.getActionType().toLowerCase()+".png");
        actionTitle.setText(action.getActionType().toUpperCase());
        actionIcon.setImage(actionImage);
        entityField.setText(action.getEntity());
        if(action.getSecondEntity()!=null){
            secondEntityField.setText(action.getSecondEntity());
        }
        else {
            Image image = new Image("resources/secondEntityNull.png");
            secondEntityImg.setImage(image);
            secondEntityField.setText("None");
        }

        final AtomicInteger counter = new AtomicInteger();
        counter.set(0);
        action.getArgs().forEach((k,v)-> {
            final int index = counter.getAndIncrement();
            actionGridPane.add(new Label(k), 1, 3+index);
            TextField temp = new TextField(v);
            temp.setEditable(false);
            actionGridPane.add(temp, 2, 3+index);

        });

        for (int i = 0; i <= counter.get(); i++) {
            RowConstraints con = new RowConstraints();
            con.setPrefHeight(30);
            con.setMaxHeight(70);
            con.setVgrow(Priority.SOMETIMES);
            actionGridPane.getRowConstraints().add(con);
        }

    }

}

