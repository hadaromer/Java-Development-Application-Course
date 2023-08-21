package exceptions;

public class EntityPropertyNotFoundException extends RuntimeException {
    private String property;
    private String entity;

    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: property %s not found in entity %s";

    public EntityPropertyNotFoundException(String property, String entity, String source) {
        this.property = property;
        this.entity = entity;
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source, property, entity);
    }
}
