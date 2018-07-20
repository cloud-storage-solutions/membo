package io.membo.web.client.rss.reddit;

import com.rometools.rome.feed.synd.SyndEntry;
import io.membo.web.client.convert.Converter;
import io.membo.web.client.rss.Post;
import io.membo.web.client.rss.RedditPost;

public class CryptocurrencyRssFetcher extends RedditRssFetcher {
    public CryptocurrencyRssFetcher(Converter<SyndEntry, RedditPost> converter) {
        super("https://www.reddit.com/r/cryptocurrency/.rss", converter);
    }
}
