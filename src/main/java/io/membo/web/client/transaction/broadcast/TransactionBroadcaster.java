package io.membo.web.client.transaction.broadcast;

public interface TransactionBroadcaster {
    void broadcastTransaction(String txHex) throws TransactionBroadcastException;
}
