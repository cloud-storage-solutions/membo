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
                return;
            }

            try {
                String newTitle = post.getUrl().substring(45)
                        .replaceAll("_", " ")
                        .replaceAll("/", "");
                post.setTitle(newTitle);
                int secondsToSleep = 10;
                System.out.println("Retrying to submit post in " + secondsToSleep + " seconds ...");
                Thread.sleep(1000 * secondsToSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        throw new TransactionBroadcastException("Failed to submit post: " + post.getUrl());
    }

    private boolean submitSuccessful(Post post) throws TransactionBroadcastException, TransactionCreationException {
        try {
            String memoContent = post.toMemoPost();
            System.out.println("Memo content: " + memoContent);

            String transaction = memoTransactionCreator.createTransaction(memoContent);
            System.out.println("Transaction: " + transaction);

            transactionBroadcaster.broadcastTransaction(transaction);
        } catch (ServiceDownException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
