package exceptions;

public class InvalidMaxPopulationException extends RuntimeException {
    private String current;
    private String max;

    private final String EXCEPTION_MESSAGE = "Setting population failed: An invalid population found (%s entities). Max population is: %s";

    public InvalidMaxPopulationException(String current, String max) {
        this.current = current;
        this.max = max;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, current, max);
    }
}

