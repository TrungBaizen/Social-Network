package com.example.socialnetworkbe.model.DTO;

import com.example.socialnetworkbe.model.Like;
import com.example.socialnetworkbe.model.Profile;
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
    private String firstName;
    private String lastName;
    private Long postId;
    private LocalDateTime createdAt;

    public LikeDTO(Like like , Profile profile) {
        this.userId = like.getUser().getId();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.postId = like.getPost().getId();
        this.createdAt = like.getCreatedAt();
    }

}
