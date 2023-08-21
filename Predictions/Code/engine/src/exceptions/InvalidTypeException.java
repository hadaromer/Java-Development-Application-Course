package exceptions;

public class InvalidTypeException extends RuntimeException {
    private String invalidType;
    private String cause;
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: Type %s is invalid for %s";

    public InvalidTypeException(String invalidType, String cause, String source) {
        this.invalidType = invalidType;
        this.cause = cause;
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source, invalidType, cause);
    }
}