package io.membo.web.client.crosspost.submit.memo;

import io.membo.web.client.crosspost.submit.Submitter;
import io.membo.web.client.rss.Post;
import io.membo.web.client.transaction.broadcast.ServiceDownException;
import io.membo.web.client.transaction.broadcast.TransactionBroadcastException;
import io.membo.web.client.transaction.broadcast.TransactionBroadcaster;
import io.membo.web.client.transaction.create.TransactionCreationException;
import io.membo.web.client.transaction.create.memo.MemoTransactionCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemoSubmitter implements Submitter {
    private final MemoTransactionCreator memoTransactionCreator;
    private final TransactionBroadcaster transactionBroadcaster;

    @Autowired
    public MemoSubmitter(MemoTransactionCreator memoTransactionCreator, TransactionBroadcaster transactionBroadcaster) {
        this.memoTransactionCreator = memoTransactionCreator;
        this.transactionBroadcaster = transactionBroadcaster;
    }

    @Override
    public void submit(Post post) throws TransactionBroadcastException, TransactionCreationException {
        for (int i = 0; i < 3; ++i) {
            if (submitSuccessful(post)) {
                break;
            }
        }
    }

    private boolean submitSuccessful(Post post) throws TransactionBroadcastException, TransactionCreationException {
        try {
            transactionBroadcaster.broadcastTransaction(
                    memoTransactionCreator.createTransaction(
                            post.toMemoPost()));
        } catch (ServiceDownException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
