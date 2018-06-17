package io.membo.web.client.rss;

public class RssFetchingException extends Exception {
    public RssFetchingException() {
    }

    public RssFetchingException(String message) {
        super(message);
    }

    public RssFetchingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RssFetchingException(Throwable cause) {
        super(cause);
    }
}
