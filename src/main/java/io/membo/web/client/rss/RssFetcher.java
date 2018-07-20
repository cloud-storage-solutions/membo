package io.membo.web.client.rss;

import java.util.List;

public interface RssFetcher<T extends Post> {
    List<T> fetch() throws RssFetchingException;
}
