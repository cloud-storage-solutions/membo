package io.membo.web.client.crosspost.submit;

import io.membo.web.client.rss.Post;
import io.membo.web.client.transaction.TransactionException;

public interface Submitter {
    void submit(Post post, boolean dontPost) throws TransactionException;
}
