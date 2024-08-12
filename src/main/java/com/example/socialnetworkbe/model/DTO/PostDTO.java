package com.example.socialnetworkbe.model.DTO;

import com.example.socialnetworkbe.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    @NotBlank(message = "Email người đăng bài không được để trống")
    private String email;
    private String content;
    private List<String> postImages;
    @NotNull(message = "Trạng thái bài viết không được để trống")
    private PostStatus postStatus;
}
