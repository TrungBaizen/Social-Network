package com.example.socialnetworkbe.model.DTO;

import com.example.socialnetworkbe.model.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LikeDTO {
    private Long userId;
    private Long postId;
    private LocalDateTime createdAt;

    public LikeDTO(Like like) {
        this.userId = like.getUser().getId();
        this.postId = like.getPost().getId();
        this.createdAt = like.getCreatedAt();
    }

}
