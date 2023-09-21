package utils;

import javafx.scene.control.TextFormatter;
import validator.Utils;

public class textFormatters {

    public static TextFormatter<String> getTextFormatterByType(String type) {
        if (type.equals("decimal")) {
            return getDecimalFormatter();
        }
        if (type.equals("float")) {
            return getFloatFormatter();
        }
        return null;
    }

    private static TextFormatter<String> getDecimalFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^(?:[1-9]\\d*|)$")) {
                return change;
            } else {
                return null;
            }
        });
    }

    private static TextFormatter<String> getFloatFormatter() {
        return new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("-?\\d*\\.?\\d*")) {
                return change;
            } else {
                return null; // Reject the change if it doesn't match the desired format
            }
        });
    }

}
