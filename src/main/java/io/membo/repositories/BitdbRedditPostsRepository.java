package io.membo.repositories;

import io.membo.web.client.rss.RedditPost;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BitdbRedditPostsRepository {
    public List<RedditPost> findAll() {
        String query =
                "{\n" +
                "  \"request\": {\n" +
                "    \"encoding\": {\n" +
                "      \"b1\": \"hex\"\n" +
                "    },\n" +
                "    \"find\": {\n" +
                "      \"senders.a\": \"qqdgw5am97nz6utgltnsqhql063apwv48vxec778hn\",\n" +
                "      \"b1\": {\n" +
                "        \"$in\": [\n" +
                "          \"6d02\"\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    \"limit\": 10000\n" +
                "  }\n" +
                "};";

        List<RedditPost> posts = new ArrayList<>();
        try {
            String base64Query = new String(Base64.encodeBase64(query.getBytes()));
            RestTemplate restTemplate = new RestTemplate();
            RequestEntity<Void> build = null;
            build = RequestEntity.get(
                    new URI("https://bitdb.network/q/IHsNCiAgInJlcXVlc3QiOiB7DQogICAgImVuY29kaW5nIjogew0KICAgICAgImIxIjogImhleCINCiAgICB9LA0KICAgICJmaW5kIjogew0KICAgICAgInNlbmRlcnMuYSI6ICJxcWRndzVhbTk3bno2dXRnbHRuc3FocWwwNjNhcHd2NDh2eGVjNzc4aG4iLA0KICAgICAgImIxIjogew0KICAgICAgICAiJGluIjogWw0KICAgICAgICAgICI2ZDAyIg0KICAgICAgICBdDQogICAgICB9DQogICAgfSwNCiAgICAibGltaXQiOiAxMDAwMA0KICB9DQp9"))
                    .header("key", "qq23v3px970yxjghrth3zr7c2araac2v5v2j7ea7sj").build();
            ResponseEntity<String> exchange = restTemplate.exchange(build, String.class);
            Matcher matcher = Pattern.compile("http://redd.it/([\\w\\d]+)").matcher(exchange.getBody());

            while (matcher.find()) {
                posts.add(new RedditPost("", "", matcher.group(0), matcher.group(1)));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return posts;
    }
}
