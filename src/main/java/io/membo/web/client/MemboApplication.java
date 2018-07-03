package io.membo.web.client;

import io.membo.web.client.crosspost.RedditToMemoCrossposter;
import io.membo.web.client.rss.RssFetchingException;
import io.membo.web.client.transaction.broadcast.TransactionBroadcastException;
import io.membo.web.client.transaction.create.TransactionCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

@EnableJpaRepositories("io.membo.repositories")
@SpringBootApplication
public class MemboApplication {
    @Autowired
    private RedditToMemoCrossposter crossposter;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MemboApplication.class, args);
    }

    @PostConstruct
    public void init() throws InterruptedException, TransactionCreationException, RssFetchingException, TransactionBroadcastException {
        crossposter.crosspostAllForever(10);
    }
}
