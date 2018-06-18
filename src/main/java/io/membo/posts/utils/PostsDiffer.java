package io.membo.posts.utils;

import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;

import java.util.Set;

import org.springframework.stereotype.Component;

import io.membo.web.client.rss.Post;

@Component
public class PostsDiffer {
	public final Set<Post> getNewPosts(final Set<Post> posts, final Set<Post> fetchedPosts) {
		Set<Post> postsCopy = posts.stream().collect(toSet());
		postsCopy.removeAll(fetchedPosts);
		return unmodifiableSet(postsCopy);
	}
}
