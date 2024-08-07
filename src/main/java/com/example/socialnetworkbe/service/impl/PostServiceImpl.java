package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.Post;
import com.example.socialnetworkbe.repository.PostRepository;
import com.example.socialnetworkbe.service.PostService;
import com.example.socialnetworkbe.validate.ExceptionHandlerControllerAdvice;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post save(Post post, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        return postRepository.save(post);
    }

    @Override
    public Post update(Post post, Long id, BindingResult bindingResult, UserDetails userDetails) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        Post existingPost = findById(id).get();
        if (!existingPost.getUser().getEmail().equals(userDetails.getUsername())) {
            throw new AccessDeniedException("Bạn không phải là chủ sở hữu của bài đăng này");
        }
        post.setId(id);
        return postRepository.save(post);
    }

    @Override
    public Post delete(Long id) {
        Optional<Post> postOptional = findById(id);
        postRepository.deleteById(id);
        return postOptional.get();
    }

    @Override
    public Optional<Post> findById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            return postRepository.findById(id);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public List<Post> findAll() {
        if (postRepository.findAll().isEmpty()) {
            throw new IllegalArgumentException();
        }
        return postRepository.findAll();
    }

    @Override
    public List<Post> findAllByUserId(Long userId) {
        if (postRepository.findAllByUserId(userId).isEmpty()) {
            throw new IllegalArgumentException();
        }
        return postRepository.findAllByUserId(userId);
    }

    @Override
    public List<Post> searchPost(String content) {
        if (postRepository.findAllByContentContaining(content).isEmpty()) {
            throw new IllegalArgumentException();
        }
        return postRepository.findAllByContentContaining(content);
    }
}
