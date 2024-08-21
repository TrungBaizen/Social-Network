package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.CommentImage;

import java.util.List;

public interface CommentImageService {
    List<CommentImage> deleteAllByCommentId(Long commentId);
    List<CommentImage> findImageAllByPostId(Long postId);
    List<CommentImage> findImageAllByPostIdIn(List<Long> postIds);
    List<CommentImage> saveAll(List<CommentImage> images);
    List<CommentImage> findImageAllByCommentId(Long commentId);
}
