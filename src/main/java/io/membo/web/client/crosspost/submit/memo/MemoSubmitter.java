package io.membo.web.client.crosspost.submit.memo;

import io.membo.web.client.crosspost.submit.Submitter;
import io.membo.web.client.rss.Post;
import io.membo.web.client.transaction.TransactionException;
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
    private final int RETRY_DELAY_SECONDS = 10;

    @Autowired
    public MemoSubmitter(MemoTransactionCreator memoTransactionCreator, TransactionBroadcaster transactionBroadcaster) {
        this.memoTransactionCreator = memoTransactionCreator;
        this.transactionBroadcaster = transactionBroadcaster;
    }

    @Override
    public void submit(Post post) throws TransactionException {
        try {
            doSubmit(post);
        } catch (Exception e) {
            e.printStackTrace();
            retrySubmitWithSimplifiedName(post);
        }
    }

    private void retrySubmitWithSimplifiedName(Post post)
          throws TransactionException {
        simplifyPostName(post);

        try {
            System.out
                  .println("Retrying to submit post with simplified name in " + RETRY_DELAY_SECONDS + " seconds ...");
            Thread.sleep(1000 * RETRY_DELAY_SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            doSubmit(post);
        } catch (Exception e) {
            throw new TransactionException("Failed to submit post even with simplified name: " + post.getUrl(), e);
        }
    }

    private void simplifyPostName(Post post) {
        int startOfRedditUrlTitle = 45;
        int redditMaxUrlTitle = 55;

        String newTitle = post.getUrl().substring(startOfRedditUrlTitle)
              .replaceAll("_", " ")
              .replaceAll("/", "");

        if (newTitle.length() > redditMaxUrlTitle) {
            newTitle += Post.CUTOUT;
        }

        post.setTitle(newTitle);
    }

    private void doSubmit(Post post) throws TransactionBroadcastException, TransactionCreationException {
        String memoContent = post.toMemoPost();
        System.out.println("\nMemo content: " + memoContent);

        String transaction = memoTransactionCreator.createTransaction(memoContent);
        System.out.println("Transaction hex: " + transaction);

        transactionBroadcaster.broadcastTransaction(transaction);
    }
}
