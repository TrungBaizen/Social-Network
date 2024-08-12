package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.CommentImage;

import java.util.List;

public interface CommentImageService {
    void deleteAllByCommentId(Long commentId);
    List<CommentImage> findImageAllByPostId(Long postId);
    List<CommentImage> findImageAllByPostIdIn(List<Long> postIds);
    void saveAll(List<CommentImage> images);
}
