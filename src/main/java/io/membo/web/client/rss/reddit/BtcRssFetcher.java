package io.membo.web.client.rss.reddit;

import com.rometools.rome.feed.synd.SyndEntry;
import io.membo.web.client.convert.Converter;
import io.membo.web.client.rss.Post;

public class BtcRssFetcher extends RedditRssFetcher {
    public BtcRssFetcher(Converter<SyndEntry, Post> converter) {
        super(converter, "https://www.reddit.com/r/btc/.rss");
    }
}

