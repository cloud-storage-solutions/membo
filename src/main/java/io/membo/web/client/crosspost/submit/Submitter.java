package io.membo.web.client.crosspost.submit;

import io.membo.web.client.rss.Post;
import io.membo.web.client.transaction.broadcast.TransactionBroadcastException;
import io.membo.web.client.transaction.create.TransactionCreationException;

public interface Submitter {
    void submit(Post post) throws TransactionCreationException, TransactionBroadcastException;
}
