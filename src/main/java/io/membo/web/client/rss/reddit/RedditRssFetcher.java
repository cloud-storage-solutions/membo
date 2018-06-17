package io.membo.web.client.rss.reddit;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import io.membo.web.client.convert.Converter;
import io.membo.web.client.rss.Post;
import io.membo.web.client.rss.RssFetcher;
import io.membo.web.client.rss.RssFetchingException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class RedditRssFetcher implements RssFetcher {
    private final String rssUrl;
    private final Converter<SyndEntry, Post> converter;

    public RedditRssFetcher(Converter<SyndEntry, Post> converter, String rssUrl) {
        this.converter = converter;
        this.rssUrl = rssUrl;
    }

    public List<Post> fetch() throws RssFetchingException {
        try {
            SyndFeedInput syndFeedInput = new SyndFeedInput();
            SyndFeed feed = syndFeedInput.build(new XmlReader(new URL(rssUrl)));

            return converter.convert(feed.getEntries());
        } catch (FeedException | IOException e) {
            throw new RssFetchingException("There was a problem while fetching the rss content", e);
        }
    }
}
