package api;

public class Response {
    public enum Status{SUCCESS,ERROR}
    private Status status;
    private String message;

    public Response(Status status) {
        this.status = status;
    }

    public Response(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
