package com.example.socialnetworkbe.repository;


import com.example.socialnetworkbe.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByUserId(Long userId);
    List<Post> findAllByContentContaining(String content);
    @Query(value = "select posts.* from posts join tdh.user u on posts.user_id = u.id join tdh.follows f on u.id = f.followed_id where f.follower_id = :followerId",nativeQuery = true)
    List<Post> findAllByFollowerId(Long followerId);
}
