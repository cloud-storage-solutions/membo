package io.membo.web.client.crosspost;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RedditUrlShortener {
    public String shorten(String url) {
        Matcher matcher = Pattern.compile(".*reddit.com/r/[\\w\\d]+/comments/([\\w\\d]+)/.*").matcher(url);
        matcher.find();
        return "http://redd.it/" + matcher.group(1);
    }
}
