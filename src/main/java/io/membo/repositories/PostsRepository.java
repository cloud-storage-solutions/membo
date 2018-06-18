package io.membo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.membo.web.client.rss.Post;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long> {
}
