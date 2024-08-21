package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.DTO.*;
import com.example.socialnetworkbe.model.Post;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface PostService {
    PostLikeCommentDTO save(PostDTO postDTO, BindingResult bindingResult);

    PostLikeCommentDTO update(PostDTO postDTO, Long id, BindingResult bindingResult);

    Post delete(Long id);

    Optional<Post> findById(Long id);

    List<PostLikeCommentDTO> searchPost(String content);

    List<PostLikeCommentDTO> findAllByUserId(Long userId);

    List<PostLikeCommentDTO> findAllByFollowing(Long userId);

    LikeDTO likePost(LikeDTO likeDTO, BindingResult bindingResult);

    LikeDTO deleteLikePost(Long id);

    CommentDTO commentPost(CommentDTO commentDTO, BindingResult bindingResult);

    Long deleteCommentPost(Long id);

    CommentDTO updateCommentPost(Long id, CommentUpdateDTO commentUpdateDTO, BindingResult bindingResult);
}
