package io.membo.web.rss.fetch;

import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class MemboRssApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemboRssApplication.class, args);
		extracted();
	}

	@Scheduled(cron = "*/5 * * * * *")
	private static void extracted() {
		try {
			System.out.println(new RssFetcher().fetchFrom());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
