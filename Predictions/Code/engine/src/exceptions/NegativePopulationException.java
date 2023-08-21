package exceptions;

public class NegativePopulationException extends RuntimeException {
    private String source;
    private final String EXCEPTION_MESSAGE = "%s failed: Element PRD-population must be positive";

    public NegativePopulationException(String source) {
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, source);
    }
}

