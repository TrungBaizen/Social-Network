package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.CommentImage;
import com.example.socialnetworkbe.repository.CommentImageRepository;
import com.example.socialnetworkbe.service.CommentImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentImageServiceImpl implements CommentImageService {
    private final CommentImageRepository commentImageRepository;

    @Autowired
    public CommentImageServiceImpl(CommentImageRepository commentImageRepository) {
        this.commentImageRepository = commentImageRepository;
    }

    @Override
    public void deleteAllByCommentId(Long commentId) {
        commentImageRepository.deleteAllByCommentId(commentId);
    }

    @Override
    public List<CommentImage> findImageAllByPostId(Long postId) {
        return commentImageRepository.findImageAllByPostId(postId);
    }

    @Override
    public List<CommentImage> findImageAllByPostIdIn(List<Long> postIds) {
        return commentImageRepository.findImageAllByPostIdIn(postIds);
    }

    @Override
    public void saveAll(List<CommentImage> images) {
        commentImageRepository.saveAll(images);
    }
}
