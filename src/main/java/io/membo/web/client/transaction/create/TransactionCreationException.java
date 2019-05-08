package io.membo.web.client.transaction.create;

import io.membo.web.client.transaction.TransactionException;

public class TransactionCreationException extends TransactionException {
    public TransactionCreationException() {
    }

    public TransactionCreationException(String message) {
        super(message);
    }

    public TransactionCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionCreationException(Throwable cause) {
        super(cause);
    }
}
