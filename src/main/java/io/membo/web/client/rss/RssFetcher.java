package io.membo.web.client.rss;

import java.util.List;

public interface RssFetcher {
    List<Post> fetch() throws RssFetchingException;
}
