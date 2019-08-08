package io.membo.web.client;

import io.membo.web.client.convert.RssEntryToRedditPostConverter;
import io.membo.web.client.crosspost.RedditToMemoCrossposter;
import io.membo.web.client.crosspost.RedditUrlShortener;
import io.membo.web.client.crosspost.submit.memo.MemoSubmitter;
import io.membo.web.client.rss.reddit.BtcRssFetcher;
import io.membo.web.client.transaction.broadcast.bitbox.BitboxTransactionBroadcaster;

public class MemboApplication {
    public static void main(String[] args) throws Exception {
        int minutesDelay = args.length > 0 ? Integer.parseInt(args[0]) : 10;
        boolean dontPost = args.length > 1 ? Boolean.parseBoolean(args[1]) : false;

        if (dontPost)
            System.out.println("WARNING: Won't be actually posting on the blockchain, just displaying the new posts!");

        new RedditToMemoCrossposter(
                new MemoSubmitter(
                        new ProcessMemoTransactionCreator(),
                        new BitboxTransactionBroadcaster()),
                new BtcRssFetcher(
                        new RssEntryToRedditPostConverter(
                                new RedditUrlShortener())))
                .crosspostAllForever(minutesDelay, dontPost);
    }
}
