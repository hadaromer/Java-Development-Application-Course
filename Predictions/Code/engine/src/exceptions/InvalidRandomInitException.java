package exceptions;

public class InvalidRandomInitException extends RuntimeException {
    public enum Cause {RANDOM_AND_INIT, NO_RANDOM_OR_INIT}

    ;
    private String name;
    private Cause cause;
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: Init value is invalid, when %s is random initializes";
    private final String EXCEPTION_MESSAGE_2 = "%s failed: Init value is mandatory, when %s isn't random initializes";

    public InvalidRandomInitException(String name, Cause cause, String source) {
        this.name = name;
        this.cause = cause;
        this.source = source;
    }

    @Override
    public String getMessage() {
        switch (cause) {
            case RANDOM_AND_INIT:
                return String.format(EXCEPTION_MESSAGE, source, name);
            case NO_RANDOM_OR_INIT:
                return String.format(EXCEPTION_MESSAGE_2, source, name);
            default:
                return "";
        }
    }
}

