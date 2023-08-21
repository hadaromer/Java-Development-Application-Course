package exceptions;

public class EntityNotFoundException extends RuntimeException {
    private String name;
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: entity %s not found";

    public EntityNotFoundException(String name, String source) {
        this.name = name;
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source, name);
    }
}