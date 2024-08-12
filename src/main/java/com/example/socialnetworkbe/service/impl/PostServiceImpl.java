package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.enums.PostStatus;
import com.example.socialnetworkbe.model.*;
import com.example.socialnetworkbe.model.DTO.*;
import com.example.socialnetworkbe.repository.PostRepository;
import com.example.socialnetworkbe.repository.UserRepository;
import com.example.socialnetworkbe.service.*;
import com.example.socialnetworkbe.validate.ExceptionHandlerControllerAdvice;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostImageService postImageService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final UserService userService;


    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, PostImageService postImageService, LikeService likeService, CommentService commentService, UserService userService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postImageService = postImageService;
        this.likeService = likeService;
        this.commentService = commentService;

        this.userService = userService;
    }

    @Override
    public Post save(PostDTO postDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        String email = postDTO.getEmail();
        String content = postDTO.getContent();
        List<String> images = postDTO.getPostImages();
        PostStatus postStatus = postDTO.getPostStatus();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Tài khoản " + email + " không tồn tại.");
        }
        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setPostStatus(postStatus);
        Post postSave = postRepository.save(post);

        if (!images.isEmpty()) {
            List<PostImage> postImageList = images.stream().map(image -> {
                PostImage postImage = new PostImage();
                postImage.setPost(postSave);  // Liên kết với bài viết đã lưu
                postImage.setImage(image);
                return postImage;
            }).collect(toList());
            postImageService.saveAll(postImageList);
        }

        return postRepository.save(postSave);
    }

    @Override
    public Post update(PostDTO postDTO, Long id, BindingResult bindingResult, UserDetails userDetails) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        Post existingPost = findById(id).get();
        if (!existingPost.getUser().getEmail().equals(userDetails.getUsername())) {
            throw new IllegalArgumentException("Bạn không phải là chủ sở hữu của bài đăng này");
        }
        existingPost.setContent(postDTO.getContent());
        existingPost.setPostStatus(postDTO.getPostStatus());
        List<String> images = postDTO.getPostImages();
        if (images.isEmpty()) {
            postImageService.deleteAllByPostId(existingPost.getId());
        } else {
            postImageService.deleteAllByPostId(existingPost.getId());
            List<PostImage> postImageList = images.stream().map(image -> {
                PostImage postImage = new PostImage();
                postImage.setPost(existingPost);
                postImage.setImage(image);
                return postImage;
            }).collect(toList());
            postImageService.saveAll(postImageList);
        }
        return postRepository.save(existingPost);
    }

    @Override
    public Post delete(Long id, UserDetails userDetails) {
        Post existingPost = findById(id).get();
        String userEmail = existingPost.getUser().getEmail();
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (!existingPost.getUser().getEmail().equals(userEmail) && !isAdmin) {
            throw new IllegalArgumentException("Bạn không có quyền xóa bài đăng này");
        }
        commentService.deleteAllByPostId(existingPost.getId());
        likeService.deleteAllByPostId(existingPost.getId());
        postImageService.deleteAllByPostId(existingPost.getId());
        postRepository.deleteById(id);
        return existingPost;
    }

    @Override
    public List<PostLikeCommentDTO> findAllByUserId(Long userId) {
        // Lấy tất cả các bài viết của người dùng
        List<Post> posts = postRepository.findAllByUserId(userId);
        return findAllByPosts(posts);
    }


    @Override
    public List<PostLikeCommentDTO> searchPost(String content) {
        List<Post> posts = postRepository.findAllByContentContaining(content);
        return findAllByPosts(posts);
    }

    @Override
    public Optional<Post> findById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            return postRepository.findById(id);
        }
        throw new IllegalArgumentException("Bài viết này không tồn tại .");
    }

    private List<PostLikeCommentDTO> findAllByPosts(List<Post> posts) {
        if (posts.isEmpty()) {
            return new ArrayList<>();
        }
        // Tạo các danh sách chứa các đối tượng từ các dịch vụ khác
        List<Long> postIds = posts.stream().map(Post::getId).collect(toList());

        List<PostImageDTO> postImageDTOs = postImageService.findAllByPostIdIn(postIds).stream().map(PostImageDTO::new).toList();
        Map<Long, List<PostImageDTO>> postImageDTOsMapPostId = postImageDTOs.stream().collect(Collectors.groupingBy(PostImageDTO::getPostId));
        List<CommentImageDTO> commentsImageDTOs = commentService.findImageAllByPostIdIn(postIds).stream().map(CommentImageDTO::new).toList();
        Map<Long, List<CommentImageDTO>> commentsImageDTOMapPostId = commentsImageDTOs.stream().collect(Collectors.groupingBy(CommentImageDTO::getCommentId));
        List<CommentDTO> commentDTOS = commentService.findAllByPostIdIn(postIds).stream().map(c -> new CommentDTO(c, commentsImageDTOMapPostId)).toList();
        /*Xu ly list comment con*/
        Map<Long, List<CommentDTO>> commentChildrenDtoMapPostId = commentDTOS.stream()
                .filter(c -> Objects.nonNull(c.getParentCommentId()))
                .collect(Collectors.groupingBy(CommentDTO::getParentCommentId));
        commentDTOS.stream().filter(c -> commentChildrenDtoMapPostId.containsKey(c.getId())).forEach(c -> c.setCommentChildren(commentChildrenDtoMapPostId.get(c.getId())));

        Map<Long, List<CommentDTO>> commentParentDtoMapPostId = commentDTOS.stream().filter(c -> Objects.isNull(c.getParentCommentId())).collect(Collectors.groupingBy(CommentDTO::getPostId));
        List<LikeDTO> likeDTOs = likeService.findAllByPostIdIn(postIds).stream().map(LikeDTO::new).toList();
        Map<Long, List<LikeDTO>> likeDTOMapPostId = likeDTOs.stream().collect(Collectors.groupingBy(LikeDTO::getPostId));
        return posts.stream().map(p -> new PostLikeCommentDTO(p, postImageDTOsMapPostId.get(p.getId()), likeDTOMapPostId.get(p.getId()), commentParentDtoMapPostId.get(p.getId()))).collect(Collectors.toList());
    }

    @Override
    public void likePost(LikeDTO likeDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        Post post = findById(likeDTO.getPostId()).get();
        User user = userService.findById(likeDTO.getUserId()).get();
        if (likeService.checkLike(likeDTO.getUserId(), likeDTO.getPostId())) {
            throw new IllegalArgumentException("Bạn đã thích bài viết này rồi");
        }
        Like newLike = new Like();
        newLike.setPost(post);
        newLike.setUser(user);
        likeService.save(newLike, bindingResult);
    }

    @Override
    public void deleteLikePost(Long id) {
        likeService.delete(id);
    }

    @Override
    public void commentPost(CommentDTO commentDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }

        List<CommentDTO> commentChildren = commentDTO.getCommentChildren();
        CommentDTO comment = (commentChildren != null && !commentChildren.isEmpty())
                ? commentChildren.get(commentChildren.size() - 1)
                : commentDTO;

        Post post = findById(comment.getPostId()).get();
        User user = userService.findById(comment.getUserId()).get();

        Comment newComment = new Comment();
        newComment.setPost(post);
        newComment.setUser(user);
        newComment.setContent(comment.getContent());
        newComment.setParentCommentId(comment.getParentCommentId());

        List<CommentImageDTO> commentImageDTOList = comment.getCommentImages();
        List<CommentImage> commentImageList = new ArrayList<>();
        for (CommentImageDTO commentImageDTO : commentImageDTOList) {
            CommentImage commentImage = new CommentImage();
            commentImage.setComment(newComment);
            commentImage.setImage(commentImageDTO.getImage());
        }
        commentService.save(newComment, commentImageList, bindingResult);

    }

    @Override
    public void deleteCommentPost(Long id) {
        commentService.delete(id);
    }
}
