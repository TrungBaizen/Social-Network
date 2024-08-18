package com.example.socialnetworkbe.model.DTO;

import com.example.socialnetworkbe.model.Comment;
import com.example.socialnetworkbe.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String firstName;
    private String lastName;
    private Long parentCommentId;
    private List<CommentDTO> commentChildren;  // Assuming replyToUserId is mapped to comment.replyToUser.id in your database schema  // You can change this based on your actual database schema.  // If not, you should add a method to map replyToUserId to comment.replyToUser.id.
    private Long postId;
    private List<CommentImageDTO> commentImages;

    public CommentDTO(Comment comment, Profile profile, Map<Long,List<CommentImageDTO>> commentsImageDTOMapPostId){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.userId = comment.getUser().getId();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.parentCommentId = comment.getParentCommentId();
        this.postId = comment.getPost().getId();  // Assuming postId is mapped to comment.post.id in your database schema  // You can change this based on your actual database schema.  // If not, you should add a method to map postId to comment.post.id.  // This method should return a Map<Long, List<CommentImageDTO>> where key is comment.id and value is list of CommentImageDTOs.
        this.commentImages = commentsImageDTOMapPostId.get(comment.getId());
    }
}
