package io.membo.web.client.convert;

import com.rometools.rome.feed.synd.SyndEntry;
import io.membo.web.client.rss.Post;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class ConverterImpl implements Converter<SyndEntry, Post> {
   @Override
   public Post convert(SyndEntry entry) {
      return new Post(entry.getTitle(), entry.getLink(), shortenUrl(entry.getLink()));
   }

   @Override
   public List<Post> convert(List<SyndEntry> entries) {
      return entries.stream().map(this::convert).collect(toList());
   }

   private String shortenUrl(String longUrl) {
//      BitlyClient client = new BitlyClient("... the access token ...");
//      ShortenResponse respShort = client.shorten() //
//            .setLongUrl("https://github.com/stackmagic/bitly-api-client") //
//            .call();
      return "";
   }
}
