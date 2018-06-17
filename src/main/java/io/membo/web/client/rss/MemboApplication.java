package io.membo.web.client.rss;

import io.membo.web.client.Submitter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MemboApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MemboApplication.class, args);
		new Submitter().submit();
	}
}
