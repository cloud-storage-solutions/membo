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
    public void submit(Post post, boolean dontPost) throws TransactionException {
        try {
            doSubmit(post, dontPost);
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
            doSubmit(post, false);
        } catch (Exception e) {
            throw new TransactionException("Failed to submit post even with simplified name: " + post.getTitle(), e);
        }
    }

    private void simplifyPostName(Post post) {
        int startOfRedditUrlTitle = 45;
        int redditMaxUrlTitle = 40;

        String newTitle = post.getUrl().substring(startOfRedditUrlTitle)
              .replaceAll("_", " ")
              .replaceAll("/", "");
        newTitle = Character.toUpperCase(newTitle.charAt(0)) + newTitle.substring(1);

        if (newTitle.length() > redditMaxUrlTitle) {
            newTitle += Post.CUTOUT;
        }

        post.setTitle(newTitle);
    }

    private void doSubmit(Post post, boolean dontPost) throws TransactionBroadcastException, TransactionCreationException {
        String memoContent = post.toMemoPost();

        if (!dontPost) {
            String transaction = memoTransactionCreator.createTransaction(memoContent);
            transactionBroadcaster.broadcastTransaction(transaction);
        }

        System.out.println("\nMemo posted successfully: " + memoContent);
    }
}
