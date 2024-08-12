package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.CommentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentImageRepository extends JpaRepository<CommentImage,Long> {
    void deleteAllByCommentId(Long commentId);
    @Query(value = "select ci.* from comment_images ci join tdh.comments c on c.id = ci.comment_id join tdh.posts p on p.id = c.post_id WHERE p.id = :postId", nativeQuery = true)
    List<CommentImage> findImageAllByPostId(@Param("postId") Long postId);

    @Query(value = "select ci.* from comment_images ci" +
            " join tdh.comments c on c.id = ci.comment_id join tdh.posts p on p.id = c.post_id WHERE p.id in (:postIds)", nativeQuery = true)
    List<CommentImage> findImageAllByPostIdIn(List<Long> postIds);

    List<CommentImage> findImageAllByCommentId(Long commentId);
}
