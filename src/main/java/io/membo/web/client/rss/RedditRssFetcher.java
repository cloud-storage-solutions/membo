package io.membo.web.client.rss;

import java.net.URL;

import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class RedditRssFetcher {
	private static final String REDDIT_RSS_URL = "https://www.reddit.com/r/btc/.rss";

	public Feed fetch() {
		SyndFeedInput syndFeedInput = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(new URL("https://www.reddit.com/r/btc/.rss")));
	}
}
