package exceptions;

public class InvalidTerminationException extends RuntimeException {
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: termination is controlled by user only or by ticks and seconds (the earlier)";

    public InvalidTerminationException(String source) {
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source);
    }
}