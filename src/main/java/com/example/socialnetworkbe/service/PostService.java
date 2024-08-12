package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.DTO.CommentDTO;
import com.example.socialnetworkbe.model.DTO.LikeDTO;
import com.example.socialnetworkbe.model.DTO.PostDTO;
import com.example.socialnetworkbe.model.DTO.PostLikeCommentDTO;
import com.example.socialnetworkbe.model.Like;
import com.example.socialnetworkbe.model.Post;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post save(PostDTO postDTO, BindingResult bindingResult);

    Post update(PostDTO postDTO, Long id, BindingResult bindingResult, UserDetails userDetails);

    Post delete(Long id ,UserDetails userDetails);

    Optional<Post> findById(Long id);

    List<PostLikeCommentDTO> searchPost(String content);

    List<PostLikeCommentDTO> findAllByUserId(Long userId);

    void likePost(LikeDTO likeDTO, BindingResult bindingResult);

    void deleteLikePost(Long id);

    void commentPost(CommentDTO commentDTO, BindingResult bindingResult);

    void deleteCommentPost(Long id);
}
