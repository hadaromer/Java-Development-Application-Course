package exceptions;

public class InvalidRangeException extends RuntimeException {
    private String name;
    private String range;

    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: An invalid range found for property %s. range: %s";

    public InvalidRangeException(String name, String range, String source) {
        this.name = name;
        this.range = range;
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source, name, range);
    }
}

