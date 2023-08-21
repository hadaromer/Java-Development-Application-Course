package helpers;

import java.util.Random;

public class RandomCreator {
    private static final int MAX_LENGTH_STRING = 50;

    public static int getInt() {
        return new Random().nextInt();
    }
    public static int getInt(int from, int to) {
        return new Random().nextInt(to - from) + from;
    }
    public static boolean getBoolean() {
        return new Random().nextBoolean();
    }
    public static float getProbability() {
        return new Random().nextFloat();
    }
    public static float getFloat() {
        return new Random().nextFloat()*getInt();
    }
    public static float getFloat(float from, float to) {
        return from + new Random().nextFloat() * (to - from);
    }
    public static String getString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?,_-.() ";
        int randomLength = getInt(1, MAX_LENGTH_STRING);
        StringBuilder stringBuilder = new StringBuilder(randomLength);

        for (int i = 0; i < randomLength; i++) {
            int index = new Random().nextInt(characters.length());
            char randomSymbol = characters.charAt(index);
            stringBuilder.append(randomSymbol);
        }

        return stringBuilder.toString();
    }
}
