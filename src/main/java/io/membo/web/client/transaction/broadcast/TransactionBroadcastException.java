package io.membo.web.client.transaction.broadcast;

public class TransactionBroadcastException extends Exception {
    public TransactionBroadcastException() {
    }

    public TransactionBroadcastException(String message) {
        super(message);
    }

    public TransactionBroadcastException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionBroadcastException(Throwable cause) {
        super(cause);
    }
}
