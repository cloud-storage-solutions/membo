package io.membo.web.rss.fetch;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@Component
public class RssFetcher {
	RestTemplate restTemplate = new RestTemplate();

	private String demo() throws URISyntaxException {
		RequestEntity<Void> build = RequestEntity.get(new URI("https://www.reddit.com/r/btc/.rss"))
				.header("user-agent", "demo").build();
		ResponseEntity<String> exchange = restTemplate.exchange(build, String.class);
		return exchange.toString();

	}

	public String fetchFrom() throws URISyntaxException {
		return demo();
	}

}
