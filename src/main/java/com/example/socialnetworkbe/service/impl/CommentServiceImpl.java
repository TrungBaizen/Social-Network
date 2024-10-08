package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.Comment;
import com.example.socialnetworkbe.model.CommentImage;
import com.example.socialnetworkbe.repository.CommentRepository;
import com.example.socialnetworkbe.service.CommentImageService;
import com.example.socialnetworkbe.service.CommentService;
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
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentImageService commentImageService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, CommentImageService commentImageService) {
        this.commentRepository = commentRepository;
        this.commentImageService = commentImageService;
    }

    @Override
    public Comment save(Comment comment,BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        return commentRepository.save(comment);
    }

    @Override
    public List<CommentImage> saveImage(List<CommentImage> commentImageList, BindingResult bindingResult) {
        return commentImageService.saveAll(commentImageList);
    }

    @Override
    public Comment update(Comment comment,List<CommentImage> commentImage ,Long id, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        findById(id).get();
        comment.setId(id);
        if (commentImageService.findImageAllByCommentId(comment.getId()).isEmpty()) {
            commentImageService.saveAll(commentImage);
        }else {
            commentImageService.deleteAllByCommentId(id);
            commentImageService.saveAll(commentImage);
        }

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    @Override
    public List<CommentImage> findImageAllByPostId(Long postId) {
        return commentImageService.findImageAllByPostId(postId);
    }

    @Transactional
    @Override
    public void deleteAllByPostId(Long postId) {
        List<Comment> findAllCommentByPostId = commentRepository.findAllByPostId(postId);
        for (Comment comment : findAllCommentByPostId) {
            commentImageService.deleteAllByCommentId(comment.getId());
        }
        commentRepository.deleteAllByPostId(postId);
    }

    @Override
    public Comment delete(Long id) {
        Optional<Comment> commentOptional = findById(id);
        if (commentOptional.get().getParentCommentId() == null) {
            List<Comment> childrenComments = commentRepository.findAllByParentCommentId(id);
            if (!childrenComments.isEmpty()) {
                for (Comment comment : childrenComments) {
                    commentImageService.deleteAllByCommentId(comment.getId());
                }
                commentRepository.deleteAllByParentCommentId(id);
            }
        }
        commentImageService.deleteAllByCommentId(id);
        commentRepository.deleteById(id);
        return commentOptional.get();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            return commentRepository.findById(id);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public List<Comment> findAll() {
        if (commentRepository.findAll().isEmpty()) {
            throw new IllegalArgumentException();
        }
        return commentRepository.findAll();
    }

    @Override
    public List<CommentImage> findImageAllByPostIdIn(List<Long> postIds) {
        return commentImageService.findImageAllByPostIdIn(postIds);
    }

    @Override
    public List<Comment> findAllByPostIdIn(List<Long> postIds) {
        return commentRepository.findAllByPostIdIn(postIds);
    }
}
