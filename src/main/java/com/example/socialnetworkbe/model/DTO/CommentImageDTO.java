package com.example.socialnetworkbe.model.DTO;

import com.example.socialnetworkbe.model.CommentImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentImageDTO {
    private Long id;
    private String image;
    private Long commentId;

    public CommentImageDTO(CommentImage commentImage) {
        this.id = commentImage.getId();
        this.image = commentImage.getImage();
        this.commentId = commentImage.getComment().getId();
    }
}
