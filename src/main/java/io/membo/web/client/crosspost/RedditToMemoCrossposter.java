package io.membo.web.client.crosspost;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import io.membo.posts.utils.PostsDiffer;
import io.membo.repositories.PostsRepository;
import io.membo.web.client.crosspost.submit.memo.MemoSubmitter;
import io.membo.web.client.rss.Post;
import io.membo.web.client.rss.RssFetchingException;
import io.membo.web.client.rss.reddit.RedditRssFetcher;
import io.membo.web.client.transaction.broadcast.TransactionBroadcastException;
import io.membo.web.client.transaction.create.TransactionCreationException;

public class RedditToMemoCrossposter implements Crossposter {
	private MemoSubmitter submitter;
	private final RedditRssFetcher redditRssFetcher;

	final private Set<Post> submitted;
	private Set<Post> blackListed = new HashSet<>();

	@Autowired
	private PostsDiffer postsDiffer;

	@Autowired
	private PostsRepository postsRepository;

	public RedditToMemoCrossposter(MemoSubmitter submitter, RedditRssFetcher redditRssFetcher) {
		this.submitter = submitter;
		this.redditRssFetcher = redditRssFetcher;
		submitted = new HashSet<>(postsRepository.findAll());
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

	private void crosspostAll()
			throws RssFetchingException, TransactionBroadcastException, TransactionCreationException {
		final Set<Post> newPosts = postsDiffer.getNewPosts(submitted, redditRssFetcher.fetch());
		for (Post post : newPosts) {
			crosspostPost(post);
		}
	}

	private void crosspostPost(Post post) throws TransactionBroadcastException, TransactionCreationException {
		if (! submitted.contains(post) && !blackListed.contains(post)) {
			submitter.submit(post);
			submitted.add(post);
			postsRepository.save(post);
		}
	}
}
