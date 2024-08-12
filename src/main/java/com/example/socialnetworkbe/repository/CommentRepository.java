package com.example.socialnetworkbe.repository;


import com.example.socialnetworkbe.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> deleteAllByPostId(Long postId);
    List<Comment> findAllByPostId(Long postId);
    List<Comment> findAllByPostIdIn(List<Long> postIds);
}
