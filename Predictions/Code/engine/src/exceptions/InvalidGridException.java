package exceptions;

public class InvalidGridException extends RuntimeException{
    private String name;
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: An invalid grid found - %s. Rows and columns are between 10-100";

    public InvalidGridException(String name, String source) {
        this.name = name;
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source, name);
    }
}
