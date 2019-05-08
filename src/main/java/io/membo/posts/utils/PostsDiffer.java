package io.membo.posts.utils;

import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import io.membo.web.client.rss.Post;

@Component
public class PostsDiffer {
    public final Set<Post> getNewPosts(final Set<Post> posts, final List<Post> fetchedPosts) {
        Set<Post> postsCopy = new HashSet<>(posts);
        postsCopy.removeAll(fetchedPosts);
        return unmodifiableSet(postsCopy);
    }
}
