package exceptions;

public class EnvPropertyNotFoundException extends RuntimeException {
    private String name;
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: environment property %s not found";

    public EnvPropertyNotFoundException(String name, String source) {
        this.name = name;
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source, name);
    }
}