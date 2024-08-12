package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.PostImage;
import com.example.socialnetworkbe.repository.PostImageRepository;
import com.example.socialnetworkbe.service.PostImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostImageServiceImpl implements PostImageService {
    private final PostImageRepository postImageRepository;

    @Autowired
    public PostImageServiceImpl(PostImageRepository postImageRepository) {
        this.postImageRepository = postImageRepository;
    }


    @Override
    public void saveAll(List<PostImage> images) {
        postImageRepository.saveAll(images);
    }

    @Override
    public void deleteAllByPostId(Long postId) {
        postImageRepository.deleteAllByPostId(postId);
    }

    @Override
    public List<PostImage> findAllByPostId(Long postId) {
        return postImageRepository.findAllByPostId(postId);
    }

    @Override
    public List<PostImage> findAllByPostIdIn(List<Long> postIds) {
        return postImageRepository.findAllByPostIdIn(postIds);
    }


}
