package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage,Long> {
    void deleteAllByPostId(Long postId);
    List<PostImage> findAllByPostId(Long postId);
    List<PostImage> findAllByPostIdIn(List<Long> postIds);
}
