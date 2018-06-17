package io.membo.web.client.rss;

import java.util.Objects;

public class Post {
   private final int MAX_MEMO_LENGTH = 75;
   private final String DELIMITER = "\n";

   public Post(String title, String url, String shortUrl) {
      this.title = title;
      this.url = url;
      this.shortUrl = shortUrl;
   }

   private String title;
   private String url;
   private String shortUrl;

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Post post = (Post) o;

      return url.equals(post.url);
   }

   @Override
   public int hashCode() {
      return url.hashCode();
   }

   @Override
   public String toString() {
      if (Objects.equals(shortUrl, "")) {
         shortUrl = url;
      }

      if (shortUrl.length() > MAX_MEMO_LENGTH) {
         throw new RuntimeException("Url length greater than max memo length");
      }

      final String shortTitle =
            title.substring(0, Math.min(title.length(), MAX_MEMO_LENGTH - DELIMITER.length() - shortUrl.length()));
      return shortTitle + DELIMITER + shortUrl;
   }
}
