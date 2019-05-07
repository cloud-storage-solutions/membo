package io.membo.repositories;

import io.membo.Account;
import io.membo.web.client.rss.RedditPost;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BitdbRedditPostsRepository {
    private static final String BITDB_QUERY_URL = "https://bitdb.bitcoin.com/q/";

    public List<RedditPost> findAll() {
        String query = "{\n"
              + "  \"v\": 3,\n"
              + "  \"q\": {\n"
              + "    \"find\": {\n"
              + "      \"in.e.a\": \"" + Account.publicKey + "\",\n"
              + "      \"out.h1\": \"6d02\"\n"
              + "    },\n"
              + "    \"limit\": 10000\n"
              + "  }\n"
              + "}";

        List<RedditPost> posts = new ArrayList<>();
        try {
            String base64Query = new String(Base64.encodeBase64(query.getBytes()));

            StringBuilder exchange = new StringBuilder();
            URL url = new URL(BITDB_QUERY_URL + base64Query);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                exchange.append(line);
            }
            rd.close();

            Matcher matcher = Pattern.compile("http://redd.it/([\\w\\d]+)").matcher(exchange.toString());

            while (matcher.find()) {
                posts.add(new RedditPost("", "", matcher.group(0), matcher.group(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return posts;
    }
}
