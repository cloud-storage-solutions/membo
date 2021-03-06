package io.membo.web.client.rss.reddit;

import com.rometools.rome.feed.synd.SyndEntry;
import io.membo.web.client.convert.Converter;
import io.membo.web.client.rss.RedditPost;

public class SoccerRssFetcher extends RedditRssFetcher {
    public SoccerRssFetcher(Converter<SyndEntry, RedditPost> converter) {
        super("https://www.reddit.com/r/soccer/.rss", converter);
    }
}
