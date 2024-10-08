package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
    @Modifying
    @Transactional
    void deleteAllByPostId(Long postId);
    List<Like> findAllByPostId(Long postId);
    List<Like> findAllByPostIdIn(List<Long> postIds);
    boolean existsByUserIdAndPostId(Long userId, Long postId);
}
