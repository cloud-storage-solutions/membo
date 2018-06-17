package io.membo.web.client.crosspost;

import io.membo.web.client.rss.RssFetchingException;
import io.membo.web.client.transaction.broadcast.TransactionBroadcastException;
import io.membo.web.client.transaction.create.TransactionCreationException;

public interface Crossposter {
    void crosspostAllForever(int minutesDelay) throws InterruptedException, RssFetchingException,
            TransactionBroadcastException, TransactionCreationException;

    void crosspostAll(int cycles, int minutesDelay) throws InterruptedException, RssFetchingException,
            TransactionBroadcastException, TransactionCreationException;
}
