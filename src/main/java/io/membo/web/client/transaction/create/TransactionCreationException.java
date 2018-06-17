package io.membo.web.client.transaction.create;

public class TransactionCreationException extends Exception {
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
