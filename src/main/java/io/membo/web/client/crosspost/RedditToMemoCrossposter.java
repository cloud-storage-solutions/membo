package io.membo.web.client.crosspost;

import io.membo.repositories.BitdbRedditPostsRepository;
import io.membo.web.client.crosspost.submit.memo.MemoSubmitter;
import io.membo.web.client.rss.RedditPost;
import io.membo.web.client.rss.RssFetchingException;
import io.membo.web.client.rss.reddit.RedditRssFetcher;
import io.membo.web.client.transaction.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RedditToMemoCrossposter implements Crossposter {
    private final MemoSubmitter submitter;
    private final RedditRssFetcher redditRssFetcher;

    private BitdbRedditPostsRepository bitdbPostsRepository = new BitdbRedditPostsRepository();

    private Set<RedditPost> submitted;

    @Autowired
    public RedditToMemoCrossposter(MemoSubmitter submitter, RedditRssFetcher redditRssFetcher) {
        this.submitter = submitter;
        this.redditRssFetcher = redditRssFetcher;
    }

    @Override
    public void crosspostAllForever(int minutesDelay) throws InterruptedException {
        crosspostRepeatedly(-1, minutesDelay);
    }

    @Override
    public void crosspostAll(int cycles, int minutesDelay) throws InterruptedException {
        if (cycles < 1) {
            throw new RuntimeException("The number of cycles should be a positive number: " + cycles);
        }

        crosspostRepeatedly(cycles, minutesDelay);
    }

    private void crosspostRepeatedly(int repeat, int minutesDelay) throws InterruptedException {
        submitted = new HashSet<>(bitdbPostsRepository.findAll());

        for (long i = 0; i != repeat; ++i) {
            try {
                crosspostAll();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Sleeping for " + minutesDelay + " minute(s), before checking for new posts...");
            Thread.sleep(1000 * 60 * minutesDelay);
        }
    }

    private void crosspostAll() throws RssFetchingException {
        List<RedditPost> newPosts = redditRssFetcher.fetch();
        newPosts.removeAll(submitted);

        System.out.println("There are " + newPosts.size() + " new post(s) to submit.");
        for (RedditPost post : newPosts) {
            try {
                crosspostPost(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void crosspostPost(RedditPost post) throws TransactionException {
        submitter.submit(post);
        submitted.add(post);
    }
}
