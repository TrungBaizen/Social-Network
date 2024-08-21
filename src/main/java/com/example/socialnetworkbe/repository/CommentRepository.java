package com.example.socialnetworkbe.repository;


import com.example.socialnetworkbe.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Modifying
    @Transactional
    List<Comment> deleteAllByPostId(Long postId);
    List<Comment> findAllByPostId(Long postId);
    List<Comment> findAllByPostIdIn(List<Long> postIds);
    List<Comment> findAllByParentCommentId(Long parentId);
    void deleteAllByParentCommentId(Long parentId);
}
