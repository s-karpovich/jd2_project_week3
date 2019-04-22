package by.tut.mdcatalog.service.exception;

public class RequestException extends RuntimeException {
    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
