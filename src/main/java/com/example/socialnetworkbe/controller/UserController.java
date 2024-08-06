package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.model.DTO.PasswordChangeRequest;
import com.example.socialnetworkbe.model.JwtResponse;
import com.example.socialnetworkbe.model.Role;
import com.example.socialnetworkbe.model.User;
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
    public ResponseEntity createUser(@Validated @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Iterable<User> users = userService.findAll();
        for (User currentUser : users) {
            if (currentUser.getEmail().equals(user.getEmail())) {
                return new ResponseEntity<>("Username existed", HttpStatus.OK);
            }
        }
        if (!userService.isCorrectConfirmPassword(user)) {
            return new ResponseEntity<>("Input confirm password", HttpStatus.OK);
        }
        if (user.getRoles() == null) {
            Role role1 = roleService.findByName("ROLE_USER");
            Set<Role> roles1 = new HashSet<>();
            roles1.add(role1);
            user.setRoles(roles1);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        userService.save(user);
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
        userService.setActive(currentUser,true);
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
        return new ResponseEntity<>("Hiện chưa có tài khoản nào đăng ký Email này",HttpStatus.BAD_REQUEST);
    }
}