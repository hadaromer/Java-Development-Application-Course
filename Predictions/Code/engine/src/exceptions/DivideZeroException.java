package exceptions;

public class DivideZeroException extends RuntimeException {
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: division by zero is prohibited";

    public DivideZeroException(String source) {
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source);
    }
}

