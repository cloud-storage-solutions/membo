package io.membo.web.client;

import io.membo.web.client.convert.RssEntryToPostConverter;
import io.membo.web.client.convert.RssEntryToRedditPostConverter;
import io.membo.web.client.crosspost.RedditToMemoCrossposter;
import io.membo.web.client.crosspost.RedditUrlShortener;
import io.membo.web.client.crosspost.submit.memo.MemoSubmitter;
import io.membo.web.client.rss.RssFetchingException;
import io.membo.web.client.rss.reddit.BtcRssFetcher;
import io.membo.web.client.transaction.broadcast.TransactionBroadcastException;
import io.membo.web.client.transaction.broadcast.bitbox.BitboxTransactionBroadcaster;
import io.membo.web.client.transaction.create.TransactionCreationException;
import io.membo.web.client.transaction.create.memo.ProcessMemoTransactionCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

@EnableJpaRepositories("io.membo.repositories")
@SpringBootApplication
public class MemboApplication {
    public static void main(String[] args) throws Exception {
        new RedditToMemoCrossposter(
                new MemoSubmitter(
                        new ProcessMemoTransactionCreator(),
                        new BitboxTransactionBroadcaster()),
                new BtcRssFetcher(
                        new RssEntryToRedditPostConverter(
                                new RedditUrlShortener())))
                .crosspostAllForever(1);
    }
}
