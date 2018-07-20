package io.membo.web.client.rss;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class RedditPost extends Post {
	@Id
	@GeneratedValue
	private String postId;

	public RedditPost(String title, String url, String shortUrl, String postId) {
		super(title, url, shortUrl);
		this.postId = postId;
	}

	public String getPostId() {
		return postId;

	}

    @Override
    public int hashCode() {
        return postId.hashCode();
    }

    @Override
	public boolean equals(Object o) {
		RedditPost post = (RedditPost) o;
		return super.equals(o) || postId.equals(post.getPostId());
	}
}
