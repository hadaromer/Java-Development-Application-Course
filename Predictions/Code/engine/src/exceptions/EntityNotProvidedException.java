package exceptions;

public class EntityNotProvidedException extends RuntimeException {
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: entity is not provided";

    public EntityNotProvidedException(String source) {
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source);
    }
}