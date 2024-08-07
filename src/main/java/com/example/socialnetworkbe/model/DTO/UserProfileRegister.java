package com.example.socialnetworkbe.model.DTO;


import com.example.socialnetworkbe.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfileRegister {
    @NotNull(message = "Giới tính không được để trống")
    private Gender gender;
    @NotBlank(message = "Họ không được để trống")
    private String firstName;
    @NotBlank(message = "Tên không được để trống")
    private String lastName;
    @NotNull(message = "Ngày sinh không để trống")
    private LocalDate birthDate;
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$",message = "Sai định dạng mail")
    @NotBlank(message = "Email không được để trống")
    private String email;
    @NotBlank(message = "Mật khẩu không để trống")
    private String password;
    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;
}
