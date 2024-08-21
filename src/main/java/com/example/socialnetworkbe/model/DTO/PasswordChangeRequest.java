package com.example.socialnetworkbe.model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordChangeRequest {
    @NotBlank(message = "Email không để trống")
    private String email;
    @NotBlank(message = "Mật khẩu cũ không để trống")
    private String oldPassword;
    @NotBlank(message = "Mật khẩu mới không để trống")
    private String newPassword;
    @NotBlank(message = "Xác nhận mật khẩu mới không để trống")
    private String confirmPassword;
}
