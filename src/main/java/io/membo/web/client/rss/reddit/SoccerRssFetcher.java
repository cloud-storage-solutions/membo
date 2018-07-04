package io.membo.web.client.rss.reddit;

import com.rometools.rome.feed.synd.SyndEntry;
import io.membo.web.client.convert.Converter;
import io.membo.web.client.rss.Post;
import org.springframework.stereotype.Component;

public class SoccerRssFetcher extends RedditRssFetcher {
    public SoccerRssFetcher(Converter<SyndEntry, Post> converter) {
        super("https://www.reddit.com/r/soccer/.rss");
    }
}
