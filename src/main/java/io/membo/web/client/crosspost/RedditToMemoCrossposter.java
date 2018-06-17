package io.membo.web.client.crosspost;

import io.membo.web.client.crosspost.submit.memo.MemoSubmitter;
import io.membo.web.client.rss.Post;
import io.membo.web.client.rss.RssFetchingException;
import io.membo.web.client.rss.reddit.RedditRssFetcher;
import io.membo.web.client.transaction.broadcast.TransactionBroadcastException;
import io.membo.web.client.transaction.create.TransactionCreationException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RedditToMemoCrossposter implements Crossposter {
    private MemoSubmitter submitter;
    private final RedditRssFetcher redditRssFetcher;

    private Set<Post> submitted = new HashSet<>();
    private Set<Post> filtered = new HashSet<>();

    public RedditToMemoCrossposter(MemoSubmitter submitter, RedditRssFetcher redditRssFetcher) {
        this.submitter = submitter;
        this.redditRssFetcher = redditRssFetcher;
    }

    @Override
    public void crosspostAllForever(int minutesDelay) throws RssFetchingException, InterruptedException,
            TransactionBroadcastException, TransactionCreationException {
        crosspostRepeatedly(-1, minutesDelay);
    }

    @Override
    public void crosspostAll(int cycles, int minutesDelay) throws InterruptedException, RssFetchingException,
            TransactionBroadcastException, TransactionCreationException {
        if (cycles < 1) {
            throw new RuntimeException("The number of cycles should be a positive number: " + cycles);
        }

        crosspostRepeatedly(cycles, minutesDelay);
    }

    private void crosspostRepeatedly(int repeat, int minutesDelay) throws InterruptedException, RssFetchingException,
            TransactionBroadcastException, TransactionCreationException {
        for (int i = 0; i != repeat; ++i) {
            crosspostAll();
            Thread.sleep(1000 * 60 * minutesDelay);
        }
    }

    private void crosspostAll() throws RssFetchingException, TransactionBroadcastException,
            TransactionCreationException {
        List<Post> posts = redditRssFetcher.fetch();

        for (Post post : posts) {
            crosspostPost(post);
        }
    }

    private void crosspostPost(Post post) throws TransactionBroadcastException, TransactionCreationException {
        if (!submitted.contains(post) && !filtered.contains(post)) {
            submitter.submit(post);
            submitted.add(post);
        }
    }
}
