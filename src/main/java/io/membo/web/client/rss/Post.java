package io.membo.web.client.rss;

import com.rometools.utils.Strings;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Post {
	@Id
	@GeneratedValue
	private Long id;
	private String title;
	private String url;
	private String shortUrl;

	public Post() {
	}

	public Post(String title, String url, String shortUrl) {
		this.title = title;
		this.url = url;
		this.shortUrl = shortUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Post post = (Post) o;

		return url.equals(post.url);
	}

	@Override
	public int hashCode() {
		return url.hashCode();
	}

	public String toMemoPost() throws Exception { // TODO: move out
		final int MAX_MEMO_LENGTH = 75;
		final String DELIMITER = "\\n";

        if (Strings.isEmpty(shortUrl)) {
            shortUrl = url;
        }

		if (shortUrl.length() > MAX_MEMO_LENGTH) {
			throw new Exception("Url length (" + shortUrl.length() + ") is greater than max memo length ("
					+ MAX_MEMO_LENGTH + ")."); // TODO: use a better exception
		}

		String shortTitle = title.substring(0,
				Math.min(title.length(), MAX_MEMO_LENGTH - DELIMITER.length() - shortUrl.length()));
		return shortTitle + DELIMITER + shortUrl;
	}
}
