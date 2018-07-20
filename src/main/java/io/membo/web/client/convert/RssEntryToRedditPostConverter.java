package io.membo.web.client.convert;

import com.rometools.rome.feed.synd.SyndEntry;
import io.membo.web.client.crosspost.RedditUrlShortener;
import io.membo.web.client.rss.RedditPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class RssEntryToRedditPostConverter implements Converter<SyndEntry, RedditPost> {
    private final RedditUrlShortener redditUrlShortener;

    @Autowired
    public RssEntryToRedditPostConverter(RedditUrlShortener redditUrlShortener) {
        this.redditUrlShortener = redditUrlShortener;
    }

    @Override
    public RedditPost convert(SyndEntry entry) {
        String title = entry.getTitle().replaceAll("'", "\\\\'")
                .substring(0, Math.min(entry.getTitle().length(), 250));
        return new RedditPost(title, entry.getLink(), redditUrlShortener.shorten(entry.getLink()),
                redditUrlShortener.getRedditPostId(entry.getLink()));
    }

    @Override
    public List<RedditPost> convert(List<SyndEntry> entries) {
        return entries.stream().map(this::convert).collect(toList());
    }
}
