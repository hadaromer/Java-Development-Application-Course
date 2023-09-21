package exceptions;

public class EvaluateException extends RuntimeException {
    private String name;
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: wanted entity %s is not in this context";

    public EvaluateException(String name, String source) {
        this.name = name;
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source, name);
    }
}