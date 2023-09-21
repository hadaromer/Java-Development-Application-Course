package exceptions;

public class ThreadpoolSizeException extends RuntimeException {
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: threadpool count must be greater than 1";

    public ThreadpoolSizeException(String source) {
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source);
    }
}

