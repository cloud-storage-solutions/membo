package io.membo.web.client.rss;

import java.net.URL;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@SpringBootApplication
public class MemboApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MemboApplication.class, args);
		// String fetchFrom = new RedditRssFetcher().fetchFrom("/r/btc/.rss");
		// System.out.println(fetchFrom);
		// SyndFeed feed = new SyndFeedInput().build(new XmlReader(new
		// ByteArrayInputStream(fetchFrom.getBytes())));
		// System.out.println(feed.getTitle());
		for (int i = 0; i < 100; i++) {
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(new URL("https://www.reddit.com/r/btc/.rss")));
			List<SyndEntry> entries = feed.getEntries();
			for (SyndEntry entry : entries) {
				System.out.println(entry.getTitle());
				System.out.println(entry.getLink());
			}
		}
	}
}
