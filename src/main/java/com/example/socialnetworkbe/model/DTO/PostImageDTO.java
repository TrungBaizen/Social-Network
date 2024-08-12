package com.example.socialnetworkbe.model.DTO;

import com.example.socialnetworkbe.model.PostImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostImageDTO {
    private Long postId;
    private String image;

    public PostImageDTO(PostImage postImage) {
        this.postId = postImage.getPost().getId();
        this.image = postImage.getImage();
    }
}
