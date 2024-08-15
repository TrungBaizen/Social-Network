package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.Like;
import com.example.socialnetworkbe.model.Post;
import com.example.socialnetworkbe.model.User;
import com.example.socialnetworkbe.repository.LikeRepository;
import com.example.socialnetworkbe.service.LikeService;
import com.example.socialnetworkbe.service.PostService;
import com.example.socialnetworkbe.service.UserService;
import com.example.socialnetworkbe.validate.ExceptionHandlerControllerAdvice;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public Like save(Like like, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        return likeRepository.save(like);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        likeRepository.deleteById(id);
    }

    @Override
    public List<Like> findAllByPostId(Long postId) {
        return likeRepository.findAllByPostId(postId);
    }

    @Transactional
    @Override
    public void deleteAllByPostId(Long postId) {
        likeRepository.deleteAllByPostId(postId);
    }

    @Override
    public Optional<Like> findById(Long id) {
        return likeRepository.findById(id);
    }


    @Override
    public List<Like> findAllByPostIdIn(List<Long> postIds) {
        return likeRepository.findAllByPostIdIn(postIds);
    }

    @Override
    public boolean checkLike(Long userId, Long postId) {
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }
}
