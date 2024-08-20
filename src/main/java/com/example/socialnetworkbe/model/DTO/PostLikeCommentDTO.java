package com.example.socialnetworkbe.model.DTO;

import com.example.socialnetworkbe.enums.PostStatus;
import com.example.socialnetworkbe.model.Post;
import com.example.socialnetworkbe.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostLikeCommentDTO {
    private Long id;
    private Long userId;
    private String email;
    private String imageAvatar;
    private String firstName;
    private String lastName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PostStatus postStatus;
    private List<PostImageDTO> postImages;
    private List<LikeDTO> likes;
    private List<CommentDTO> comments;

    public PostLikeCommentDTO(Post post, Profile profile, List<PostImageDTO> relatedPostImages, List<LikeDTO> relatedLikes, List<CommentDTO> relatedComments) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.email = profile.getUser().getEmail();
        this.imageAvatar = profile.getImageAvatar();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.postStatus = post.getPostStatus();
        this.postImages = relatedPostImages;
        this.likes = relatedLikes;
        this.comments = relatedComments;
    }
}
