package io.membo.web.client.transaction.broadcast;

import io.membo.web.client.transaction.TransactionException;

public class TransactionBroadcastException extends TransactionException {
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
