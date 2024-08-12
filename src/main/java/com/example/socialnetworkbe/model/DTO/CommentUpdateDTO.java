package com.example.socialnetworkbe.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentUpdateDTO {
    private String content;
    private List<CommentImageDTO> commentImages;
}
