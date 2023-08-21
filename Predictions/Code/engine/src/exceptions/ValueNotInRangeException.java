package exceptions;

public class ValueNotInRangeException extends RuntimeException {
    private String name;
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: Init value in %s, isn't in defined range";

    public ValueNotInRangeException(String name, String source) {
        this.name = name;
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source, name);
    }
}

