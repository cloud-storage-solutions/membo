package io.membo.web.client.transaction.create;

public interface TransactionCreator {
    String createTransaction(String memoContent) throws TransactionCreationException;
}
