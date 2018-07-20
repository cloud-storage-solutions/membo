package io.membo.web.client.crosspost;

import io.membo.repositories.PostsRepository;
import io.membo.web.client.crosspost.submit.memo.MemoSubmitter;
import io.membo.web.client.rss.Post;
import io.membo.web.client.rss.RssFetchingException;
import io.membo.web.client.rss.reddit.RedditRssFetcher;
import io.membo.web.client.transaction.broadcast.TransactionBroadcastException;
import io.membo.web.client.transaction.create.TransactionCreationException;
import org.hibernate.boot.jaxb.SourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RedditToMemoCrossposter implements Crossposter {
    private final MemoSubmitter submitter;
    private final RedditRssFetcher redditRssFetcher;
    @Autowired
    private PostsRepository postsRepository;
//    @Autowired
//    private BlacklistedPostsRepository blacklistedPostsRepository;

    private Set<Post> submitted;
    private Set<Post> blacklisted = new HashSet<>();

    @Autowired
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
        submitted = new HashSet<>(postsRepository.findAll());
//        blacklisted = new HashSet<>(blacklistedPostsRepository.findAll());

        for (int i = 0; i != repeat; ++i) {
            crosspostAll();

            System.out.println("Sleeping for " + minutesDelay + " minute(s), before checking for new posts...");
            Thread.sleep(1000 * 60 * minutesDelay);
        }
    }

    private void crosspostAll()
            throws RssFetchingException, TransactionBroadcastException, TransactionCreationException, InterruptedException {
        List<Post> newPosts = redditRssFetcher.fetch();
        newPosts.removeAll(submitted);
//        newPosts.removeAll(blacklisted);

        System.out.println("There are " + newPosts.size() + " new post(s) to submit.");
        for (Post post : newPosts) {
            crosspostPost(post);
        }
    }

    private void crosspostPost(Post post) throws TransactionBroadcastException, TransactionCreationException {
        try {
            submitter.submit(post);
            submitted.add(post);
            postsRepository.save(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
