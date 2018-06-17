package io.membo.web.client.transaction.broadcast;

public class ServiceDownException extends TransactionBroadcastException {
    public ServiceDownException() {
    }

    public ServiceDownException(String message) {
        super(message);
    }

    public ServiceDownException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceDownException(Throwable cause) {
        super(cause);
    }
}
