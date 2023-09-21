package utils;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicBoolean;

public class utilsFunctions {

    public static boolean animated = false;

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    public static void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    public static boolean showConfirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(message);
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        AtomicBoolean res = new AtomicBoolean(false);
        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                res.set(true);
            } else if (response == buttonTypeNo) {
                res.set(false);
            }
        });
        return res.get();
    }


    public static void disableNodes(boolean disable, javafx.scene.Node... nodes) {
        for (javafx.scene.Node node : nodes) {
            node.setDisable(disable);
        }
    }

    public static void visibleNodes(boolean visible, javafx.scene.Node... nodes) {
        for (javafx.scene.Node node : nodes) {
            node.setVisible(visible);
        }
    }

    public static void fadeAnimation(Node node) {
        if (animated) {
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), node);
            node.setOpacity(0);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            node.setOpacity(1);
            fadeTransition.play();
        }
    }

    public static void carouselAnimation(Node from, Node to, boolean reverse) {
        if (animated) {
            int toX = -2000;
            int fromX = 2000;
            if (reverse) {
                toX *= -1;
                fromX *= -1;
            }
            TranslateTransition transitionFrom = new TranslateTransition(Duration.seconds(1), from);
            transitionFrom.setOnFinished(event -> {
                from.setVisible(false);
            });
            transitionFrom.setFromX(0);
            transitionFrom.setToX(toX);
            transitionFrom.play();
            to.setVisible(true);
            TranslateTransition transitionTo = new TranslateTransition(Duration.seconds(1), to);
            transitionTo.setFromX(fromX);
            transitionTo.setToX(0);
            transitionTo.play();
        } else {
            from.setVisible(false);
            to.setVisible(true);
        }
    }

}
