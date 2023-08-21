import validator.Utils;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Scanner;


public class UiScanner {
    protected interface ValidateInputInterface {
        boolean validate(String value, boolean isRange, float from, float to);
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static String getValidFilePathByExtension(String extension) {
        String filePath;
        File file;

        do {
            System.out.print("Enter the path of " + extension + " file: (leave empty to return to menu) ");
            filePath = scanner.nextLine();
            if (filePath.isEmpty()) return null;
            file = new File(filePath);

            if (!file.exists() || !file.isFile() || !filePath.endsWith("." + extension)) {
                System.out.println("Invalid file. Please provide a valid path to an " + extension + " file.");
            }

        } while (!file.exists() || !file.isFile() || !filePath.endsWith("." + extension));

        return filePath;
    }

    public static int getUserChoice(int maxValue) {
        int choice;
        String input;
        while (true) {
            System.out.print("\nPlease enter your choice: ");
            input = scanner.nextLine();
            if (Utils.isDecimal(input)) {
                choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= maxValue) return choice;
            }
            if (input.isEmpty()) return -1;
            System.out.println("Invalid input. Please enter a valid number from the list.");
        }
    }

    public static String getEnvPropertyValue(String type, boolean range, float from, float to) {
        switch (type) {
            case "decimal":
                return genericUserChoice((String a, boolean isRange, float min, float max) -> {
                    try {
                        int b = Integer.parseInt(a);
                        return !isRange || (b > min && b < max);
                    } catch (Exception e) {
                        return false;
                    }
                }, range, from, to);
            case "float":
                return genericUserChoice((String a, boolean isRange, float min, float max) -> {
                    try {
                        float b = Float.parseFloat(a);
                        return !isRange || (b > min && b < max);
                    } catch (Exception e) {
                        return false;
                    }
                }, range, from, to);
            case "boolean":
                return genericUserChoice((String a, boolean isRange, float min, float max)
                        -> Utils.isBoolean(a), range, from, to);
            case "string":
                return genericUserChoice((String a, boolean isRange, float min, float max) -> true, range, from, to);
        }
        return null;
    }

    public static String genericUserChoice(ValidateInputInterface validator, boolean isRange, float from, float to) {
        String choice;
        do {
            System.out.print("Please enter your choice (leave empty for random value) ");
            if (isRange) System.out.print("based on given range ");
            choice = scanner.nextLine();
            if (choice.isEmpty()) return null;
            if (!validator.validate(choice, isRange, from, to)) {
                System.out.println("Invalid input");
            }
        } while (!validator.validate(choice, isRange, from, to));
        return choice;
    }

    public static String getValidStateFilePath() {
        String filePath;
        do {
            System.out.print("Enter the path you want to save the file\n" +
                    "You don't need to add the file extension just /path/to/your/desired/file\n" +
                    "(leave empty to return to menu)");
            filePath = scanner.nextLine();
            if (filePath.isEmpty()) return null;

            if (!isValidPath(filePath + ".ser")) {
                System.out.println("Invalid path. Please provide a valid path.");
            }

        } while (!isValidPath(filePath + ".ser"));

        return filePath;
    }

    public static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }
}

