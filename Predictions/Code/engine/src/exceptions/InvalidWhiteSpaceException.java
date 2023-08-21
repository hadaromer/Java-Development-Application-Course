package exceptions;

public class InvalidWhiteSpaceException extends RuntimeException{
    private String name;
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: An invalid white space found for %s";

    public InvalidWhiteSpaceException(String name, String source) {
        this.name = name;
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source, name);
    }
}
