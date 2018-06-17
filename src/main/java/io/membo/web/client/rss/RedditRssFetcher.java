package io.membo.web.client.rss;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import io.membo.web.client.convert.Converter;
import org.springframework.beans.factory.annotation.Autowired;

public class RedditRssFetcher {
   private static final String REDDIT_RSS_URL = "https://www.reddit.com/r/btc/.rss";

   private Converter<SyndEntry, Post> converter;

   public RedditRssFetcher(Converter<SyndEntry, Post> converter) {
      this.converter = converter;
   }

   public List<Post> fetch() throws IOException, FeedException {
      SyndFeedInput syndFeedInput = new SyndFeedInput();
      SyndFeed feed = syndFeedInput.build(new XmlReader(new URL(REDDIT_RSS_URL)));

      return converter.convert(feed.getEntries());
   }
}
