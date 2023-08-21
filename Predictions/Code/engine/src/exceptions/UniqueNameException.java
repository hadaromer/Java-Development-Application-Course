package exceptions;

public class UniqueNameException extends RuntimeException{
    private String name;
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: Name %s already exists";

    public UniqueNameException(String name, String source) {
        this.name = name;
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source, name);
    }
}
