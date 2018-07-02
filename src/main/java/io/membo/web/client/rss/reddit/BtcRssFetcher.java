package io.membo.web.client.rss.reddit;

import com.rometools.rome.feed.synd.SyndEntry;
import io.membo.web.client.convert.Converter;
import io.membo.web.client.rss.Post;
import org.springframework.stereotype.Component;

@Component
public class BtcRssFetcher extends RedditRssFetcher {
    public BtcRssFetcher(Converter<SyndEntry, Post> converter) {
        super("https://www.reddit.com/r/btc/.rss");
    }
}

