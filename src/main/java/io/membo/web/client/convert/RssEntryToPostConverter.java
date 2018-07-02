package io.membo.web.client.convert;

import com.rometools.rome.feed.synd.SyndEntry;
import io.membo.web.client.crosspost.UrlShortener;
import io.membo.web.client.rss.Post;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class RssEntryToPostConverter implements Converter<SyndEntry, Post> {
    private UrlShortener urlShortener = new UrlShortener(); //inject

    @Override
    public Post convert(SyndEntry entry) {
        return new Post(entry.getTitle(), entry.getLink(), urlShortener.shorten(entry.getLink()));
    }

    @Override
    public List<Post> convert(List<SyndEntry> entries) {
        return entries.stream().map(this::convert).collect(toList());
    }
}
