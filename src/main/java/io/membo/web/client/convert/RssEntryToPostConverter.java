package io.membo.web.client.convert;

import com.rometools.rome.feed.synd.SyndEntry;
import io.membo.web.client.crosspost.RedditUrlShortener;
import io.membo.web.client.rss.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class RssEntryToPostConverter implements Converter<SyndEntry, Post> {
    private final RedditUrlShortener redditUrlShortener;

    @Autowired
    public RssEntryToPostConverter(RedditUrlShortener redditUrlShortener) {
        this.redditUrlShortener = redditUrlShortener;
    }

    @Override
    public Post convert(SyndEntry entry) {
        String title = entry.getTitle().replaceAll("'", "\\\\'");
        return new Post(title, entry.getLink(), redditUrlShortener.shorten(entry.getLink()));
    }

    @Override
    public List<Post> convert(List<SyndEntry> entries) {
        return entries.stream().map(this::convert).collect(toList());
    }
}
