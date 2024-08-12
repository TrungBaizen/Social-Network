package com.example.socialnetworkbe.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserOAuth {
    @NotBlank(message = "Họ không được để trống")
    private String firstName;
    @NotBlank(message = "Tên không được để trống")
    private String lastName;
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$",message = "Sai định dạng mail")
    @NotBlank(message = "Email không được để trống")
    private String email;
    @NotBlank(message = "Tên không được để trống")
    private String identifier;
}
