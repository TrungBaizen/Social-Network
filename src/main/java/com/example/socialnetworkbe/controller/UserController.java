package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.enums.Gender;
import com.example.socialnetworkbe.model.*;
import com.example.socialnetworkbe.model.DTO.PasswordChangeRequest;
import com.example.socialnetworkbe.model.DTO.UserProfileRegister;
import com.example.socialnetworkbe.service.ProfileImageService;
import com.example.socialnetworkbe.service.ProfileService;
import com.example.socialnetworkbe.service.RoleService;
import com.example.socialnetworkbe.service.UserService;
import com.example.socialnetworkbe.service.impl.EmailService;
import com.example.socialnetworkbe.service.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin("*")
public class UserController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileImageService profileImageService;

    private static final String DEFAULT_IMAGE = "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";

    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> showAllUser() {
        Iterable<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<Iterable<User>> showAllUserByAdmin() {
        Iterable<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity createUser(@Validated @RequestBody UserProfileRegister userProfileRegister, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Iterable<User> users = userService.findAll();
        for (User currentUser : users) {
            if (currentUser.getEmail().equals(userProfileRegister.getEmail())) {
                return new ResponseEntity<>("Email đã tồn tại", HttpStatus.BAD_REQUEST);
            }
        }
        User user = new User();
        user.setEmail(userProfileRegister.getEmail());
        user.setPassword(userProfileRegister.getPassword());
        user.setConfirmPassword(userProfileRegister.getConfirmPassword());
        if (!userService.isCorrectConfirmPassword(user)) {
            return new ResponseEntity<>("Mật khẩu xác nhận sai", HttpStatus.BAD_REQUEST);
        }
        if (user.getRoles() == null) {
            Role role1 = roleService.findByName("ROLE_USER");
            Set<Role> roles1 = new HashSet<>();
            roles1.add(role1);
            user.setRoles(roles1);
        }
        user.setPassword(passwordEncoder.encode(userProfileRegister.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(userProfileRegister.getConfirmPassword()));
        userService.save(user);
        User currentUser = userService.findByEmail(user.getEmail());
        Gender gender = userProfileRegister.getGender();
        String firstName = userProfileRegister.getFirstName();
        String lastName = userProfileRegister.getLastName();
        String avatar = "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";
        LocalDate birthDate = userProfileRegister.getBirthDate();
        Profile profile = new Profile();
        profile.setUser(currentUser);
        profile.setGender(gender);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setBirthDate(birthDate);
        profileService.save(profile,bindingResult);

        // Tạo profile image với ảnh mặc định
        ProfileImage profileImage = new ProfileImage();
        profileImage.setProfile(profile);
        profileImage.setImage(DEFAULT_IMAGE);
        profileImageService.save(profileImage,bindingResult);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("token") String token) {
        User user = userService.findByVerificationToken(token);
        if (user == null) {
            return new ResponseEntity<>("Invalid verification token", HttpStatus.BAD_REQUEST);
        }

        if (user.isEnabled()) {
            return new ResponseEntity<>("Account already verified", HttpStatus.OK);
        }

        userService.verifyUser(user);

        return new ResponseEntity<>("Account verified successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // kiểm tra tài khoản mật khẩu
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        // đúng thì tạo ra SecurityContextHolder để lưu trữ đối tượng đang đăng nhập
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // tạo ra token
        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findByEmail(user.getEmail());
        userService.setActive(currentUser, true);
        return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(@RequestBody User user) {
        User currentUser = userService.findByEmail(user.getEmail());
        userService.setActive(currentUser, false);
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }

    @PostMapping("/users/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        // Xác thực mật khẩu cũ
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(passwordChangeRequest.getEmail(), passwordChangeRequest.getOldPassword())
            );
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra xem mật khẩu mới và xác nhận mật khẩu mới có khớp nhau không
        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
            return new ResponseEntity<>("New passwords do not match", HttpStatus.BAD_REQUEST);
        }

        // Tìm người dùng và cập nhật mật khẩu mới
        User currentUser = userService.findByEmail(passwordChangeRequest.getEmail());
        currentUser.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        currentUser.setConfirmPassword(passwordEncoder.encode(passwordChangeRequest.getConfirmPassword()));
        userService.update(currentUser);

        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity("Hello World", HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        Optional<User> userOptional = this.userService.findById(id);
        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long id, @RequestBody User user) {
        Optional<User> userOptional = this.userService.findById(id);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.setId(userOptional.get().getId());
        user.setEmail(userOptional.get().getEmail());
        user.setEnabled(userOptional.get().isEnabled());
        user.setPassword(userOptional.get().getPassword());
        user.setRoles(userOptional.get().getRoles());
        user.setConfirmPassword(userOptional.get().getConfirmPassword());

        userService.update(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        if (userService.findByEmail(email) != null) {
            String newPassword = userService.resetPassword(email);
            User currentUser = userService.findByEmail(email);
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            currentUser.setConfirmPassword(passwordEncoder.encode(newPassword));
            userService.update(currentUser);
            return ResponseEntity.ok("Password đã được reset vui lòng vào mail để kiểm tra");
        }
        return new ResponseEntity<>("Hiện chưa có tài khoản nào đăng ký Email này", HttpStatus.BAD_REQUEST);
    }
}