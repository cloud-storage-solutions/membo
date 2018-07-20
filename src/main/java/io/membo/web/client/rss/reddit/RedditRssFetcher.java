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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class RedditRssFetcher implements RssFetcher {
    private final String rssUrl;
    @Autowired
    private Converter<SyndEntry, Post> converter;

    public RedditRssFetcher(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public List<Post> fetch() throws RssFetchingException {
        SyndFeed feed = null;

        for (int i = 0; i < 3; ++i) {
            try {
                SyndFeedInput syndFeedInput = new SyndFeedInput();
                feed = syndFeedInput.build(new XmlReader(new URL(rssUrl)));
            } catch (FeedException | IOException e) {
                if (i != 2) {
                    int secondsToSleep = 30;
                    System.out.println("Retrying to fetch RSS data in " + secondsToSleep + " seconds ...");
                    try {
                        Thread.sleep(1000 * secondsToSleep);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    throw new RssFetchingException("There was a problem while fetching the RSS content", e);
                }
            }
        }

        return converter.convert(feed.getEntries());
    }
}
