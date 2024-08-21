package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.enums.PostStatus;
import com.example.socialnetworkbe.model.*;
import com.example.socialnetworkbe.model.DTO.*;
import com.example.socialnetworkbe.repository.PostRepository;
import com.example.socialnetworkbe.repository.UserRepository;
import com.example.socialnetworkbe.service.*;
import com.example.socialnetworkbe.validate.ExceptionHandlerControllerAdvice;
import jakarta.validation.ValidationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final ProfileService profileService;


    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, PostImageService postImageService, LikeService likeService, CommentService commentService, UserService userService, ProfileService profileService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postImageService = postImageService;
        this.likeService = likeService;
        this.commentService = commentService;
        this.userService = userService;
        this.profileService = profileService;
    }

    @Override
    public PostLikeCommentDTO save(PostDTO postDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }

        String email = postDTO.getEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Tài khoản " + email + " không tồn tại.");
        }

        Post post = new Post();
        post.setUser(user);
        post.setContent(postDTO.getContent());
        post.setPostStatus(postDTO.getPostStatus());
        Post postSave = postRepository.save(post);

        List<PostImage> postImageList = postDTO.getPostImages().stream().map(image -> {
            PostImage postImage = new PostImage();
            postImage.setPost(postSave);
            postImage.setImage(image);
            return postImage;
        }).collect(Collectors.toList());

        if (!postImageList.isEmpty()) {
            postImageService.saveAll(postImageList);
        }

        Profile profile = profileService.findByUserId(user.getId());
        List<PostImageDTO> postImageDTOList = postImageList.stream().map(postImage -> {
            PostImageDTO postImageDTO = new PostImageDTO();
            postImageDTO.setPostId(postImage.getPost().getId());
            postImageDTO.setImage(postImage.getImage());
            return postImageDTO;
        }).collect(Collectors.toList());

        return new PostLikeCommentDTO(post, profile, postImageDTOList, new ArrayList<>(), new ArrayList<>());
    }


    @Override
    public PostLikeCommentDTO update(PostDTO postDTO, Long id, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        Post existingPost = findById(id).get();
        existingPost.setContent(postDTO.getContent());
        existingPost.setPostStatus(postDTO.getPostStatus());
        List<String> images = postDTO.getPostImages();
        List<PostImage> postImageList = new ArrayList<>();
        if (images.isEmpty()) {
            postImageService.deleteAllByPostId(existingPost.getId());
        } else {
            postImageService.deleteAllByPostId(existingPost.getId());
            postImageList = images.stream().map(image -> {
                PostImage postImage = new PostImage();
                postImage.setPost(existingPost);
                postImage.setImage(image);
                return postImage;
            }).collect(toList());
            postImageService.saveAll(postImageList);
        }
        postRepository.save(existingPost);
        Profile profile = profileService.findByUserId(existingPost.getUser().getId());
        List<PostImageDTO> postImageDTOList = postImageList.stream().map(postImage -> {
            PostImageDTO postImageDTO = new PostImageDTO();
            postImageDTO.setPostId(postImage.getPost().getId());
            postImageDTO.setImage(postImage.getImage());
            return postImageDTO;
        }).collect(Collectors.toList());
        List<CommentImageDTO> commentsImageDTOs = commentService.findImageAllByPostId(existingPost.getId()).stream().map(CommentImageDTO::new).toList();
        Map<Long, List<CommentImageDTO>> commentsImageDTOMapPostId = commentsImageDTOs.stream().collect(Collectors.groupingBy(CommentImageDTO::getCommentId));
        List<CommentDTO> commentDTOS = commentService.findAllByPostId(existingPost.getId()).stream().map(c -> new CommentDTO(c, profile, commentsImageDTOMapPostId)).toList();
        Map<Long, List<CommentDTO>> commentChildrenDtoMapPostId = commentDTOS.stream()
                .filter(c -> Objects.nonNull(c.getParentCommentId()))
                .collect(Collectors.groupingBy(CommentDTO::getParentCommentId));
        commentDTOS.stream().filter(c -> commentChildrenDtoMapPostId.containsKey(c.getId())).forEach(c -> c.setCommentChildren(commentChildrenDtoMapPostId.get(c.getId())));
        List<LikeDTO> likeDTOs = likeService.findAllByPostId(existingPost.getId()).stream().map(l -> new LikeDTO(l, profileService.findByUserId(l.getUser().getId()))).toList();
        return new PostLikeCommentDTO(existingPost, profile, postImageDTOList, likeDTOs, commentDTOS);
    }

    @Override
    public Post delete(Long id) {
        Post existingPost = findById(id).get();
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
        posts.sort((post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt()));
        return findAllByPosts(posts);
    }

    @Override
    public List<PostLikeCommentDTO> findAllByFollowing(Long userId) {
        List<Post> posts = postRepository.findAllByFollowerId(userId);
        posts.addAll(postRepository.findAllByUserId(userId));
        posts.sort((post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt()));
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
        List<CommentDTO> commentDTOS = commentService.findAllByPostIdIn(postIds).stream().map(c -> new CommentDTO(c, profileService.findByUserId(c.getUser().getId()), commentsImageDTOMapPostId)).toList();
        /*Xu ly list comment con*/
//        Kieu 1
        Map<Long, List<CommentDTO>> commentChildrenDtoMapPostId = commentDTOS.stream()
                .filter(c -> Objects.nonNull(c.getParentCommentId()))
                .collect(Collectors.groupingBy(CommentDTO::getParentCommentId));
//        Kieu 2:
//        Map<Long, CommentDTO> commentChildrenDtoMapPostIds = commentDTOS.stream()
//                .filter(c -> Objects.nonNull(c.getParentCommentId()))
//                .collect(Collectors.toMap(CommentDTO::getParentCommentId, v -> v , (u1,u2) -> u1));
        commentDTOS.stream().filter(c -> commentChildrenDtoMapPostId.containsKey(c.getId())).forEach(c -> c.setCommentChildren(commentChildrenDtoMapPostId.get(c.getId())));

        Map<Long, List<CommentDTO>> commentParentDtoMapPostId = commentDTOS.stream().filter(c -> Objects.isNull(c.getParentCommentId())).collect(Collectors.groupingBy(CommentDTO::getPostId));
        List<LikeDTO> likeDTOs = likeService.findAllByPostIdIn(postIds).stream().map(l -> new LikeDTO(l, profileService.findByUserId(l.getUser().getId()))).toList();
        Map<Long, List<LikeDTO>> likeDTOMapPostId = likeDTOs.stream().collect(Collectors.groupingBy(LikeDTO::getPostId));
        return posts.stream().map(p -> new PostLikeCommentDTO(p, profileService.findByUserId(p.getUser().getId()), postImageDTOsMapPostId.get(p.getId()), likeDTOMapPostId.get(p.getId()), commentParentDtoMapPostId.get(p.getId()))).collect(Collectors.toList());
    }

    @Override
    public LikeDTO likePost(LikeDTO likeDTO, BindingResult bindingResult) {
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
        Like like = likeService.save(newLike, bindingResult);
        Profile profile = profileService.findByUserId(likeDTO.getUserId());
        return new LikeDTO(like, profile);
    }

    @Override
    public LikeDTO deleteLikePost(Long id) {
        Like like = likeService.delete(id);
        Profile profile = profileService.findByUserId(like.getUser().getId());
        return new LikeDTO(like, profile);
    }

    @Override
    public CommentDTO commentPost(CommentDTO commentDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        Post post = findById(commentDTO.getPostId()).get();
        User user = userService.findById(commentDTO.getUserId()).get();

        Comment newComment = new Comment();
        newComment.setPost(post);
        newComment.setUser(user);
        newComment.setContent(commentDTO.getContent());
        newComment.setParentCommentId(commentDTO.getParentCommentId());
        Comment comment = commentService.save(newComment, bindingResult);
        List<CommentImageDTO> commentImageDTOList =
                (commentDTO.getCommentImages() == null || commentDTO.getCommentImages().isEmpty())
                        ? new ArrayList<>()
                        : commentDTO.getCommentImages();

        List<CommentImage> commentImageList = new ArrayList<>();
        if (!commentImageDTOList.isEmpty()) {
            for (CommentImageDTO commentImageDTO : commentImageDTOList) {
                CommentImage commentImage = new CommentImage();
                commentImage.setComment(newComment);
                commentImage.setImage(commentImageDTO.getImage());
                commentImageList.add(commentImage);
            }
        }
        commentService.saveImage(commentImageList,bindingResult);
        Profile profile = profileService.findByUserId(commentDTO.getUserId());
        return new CommentDTO(comment, profile, commentImageDTOList);
    }

    @Override
    public Long deleteCommentPost(Long id) {
        Comment comment = commentService.delete(id);
        return comment.getId();
    }

    @Override
    public CommentDTO updateCommentPost(Long id, CommentUpdateDTO commentUpdateDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            List<String> errors = ExceptionHandlerControllerAdvice.getMessageError(bindingResult);
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        Comment comment = commentService.findById(id).get();
        comment.setContent(commentUpdateDTO.getContent());
        List<CommentImageDTO> commentImageDTOList =
                (commentUpdateDTO.getCommentImages() == null || commentUpdateDTO.getCommentImages().isEmpty())
                        ? new ArrayList<>()
                        : commentUpdateDTO.getCommentImages();

        List<CommentImage> commentImageList = new ArrayList<>();
        if (!commentImageDTOList.isEmpty()) {
            for (CommentImageDTO commentImageDTO : commentImageDTOList) {
                CommentImage commentImage = new CommentImage();
                commentImage.setComment(comment);
                commentImage.setImage(commentImageDTO.getImage());
                commentImageList.add(commentImage);
            }
        }
        Comment commentUpdate = commentService.update(comment, commentImageList, id, bindingResult);
        Profile profile = profileService.findByUserId(commentUpdateDTO.getUserId());
        return new CommentDTO(commentUpdate, profile, commentImageDTOList);
    }
}
