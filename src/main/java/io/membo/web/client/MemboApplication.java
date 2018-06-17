package io.membo.web.client;

import io.membo.web.client.convert.RssEntryToPostConverter;
import io.membo.web.client.crosspost.RedditToMemoCrossposter;
import io.membo.web.client.crosspost.submit.memo.MemoSubmitter;
import io.membo.web.client.rss.reddit.BtcRssFetcher;
import io.membo.web.client.transaction.broadcast.bitbox.BitboxTransactionBroadcaster;
import io.membo.web.client.transaction.create.memo.ProcessMemoTransactionCreator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MemboApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MemboApplication.class, args);

        new RedditToMemoCrossposter(
                new MemoSubmitter(
                        new ProcessMemoTransactionCreator(),
                        new BitboxTransactionBroadcaster()),
                new BtcRssFetcher(
                        new RssEntryToPostConverter()))
                .crosspostAll(1, 10);
    }
}
