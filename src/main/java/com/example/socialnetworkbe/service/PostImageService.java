package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.PostImage;

import java.util.List;

public interface PostImageService {
    void saveAll(List<PostImage> images);
    void deleteAllByPostId(Long postId);
    List<PostImage> findAllByPostId(Long postId);
    List<PostImage> findAllByPostIdIn(List<Long> postIds);
}
