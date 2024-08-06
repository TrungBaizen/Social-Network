package com.example.socialnetworkbe.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordChangeRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
