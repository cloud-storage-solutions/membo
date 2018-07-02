package io.membo.web.client.transaction.broadcast;

import org.springframework.stereotype.Component;

@Component
public interface TransactionBroadcaster {
    void broadcastTransaction(String txHex) throws TransactionBroadcastException;
}
