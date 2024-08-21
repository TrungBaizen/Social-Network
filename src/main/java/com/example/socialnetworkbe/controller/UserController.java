package com.example.socialnetworkbe.controller;

import com.example.socialnetworkbe.enums.Gender;
import com.example.socialnetworkbe.enums.RegistrationType;
import com.example.socialnetworkbe.model.DTO.PasswordChangeRequest;
import com.example.socialnetworkbe.model.DTO.UserOAuth;
import com.example.socialnetworkbe.model.DTO.UserProfileRegister;
import com.example.socialnetworkbe.model.*;
import com.example.socialnetworkbe.service.ProfileService;
import com.example.socialnetworkbe.service.RoleService;
import com.example.socialnetworkbe.service.UserService;
import com.example.socialnetworkbe.service.impl.EmailService;
import com.example.socialnetworkbe.service.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
        user.setRegistrationType(RegistrationType.NORMAL);
        userService.save(user);
        User currentUser = userService.findByEmail(user.getEmail());
        Gender gender = userProfileRegister.getGender();
        String firstName = userProfileRegister.getFirstName();
        String lastName = userProfileRegister.getLastName();
        LocalDate birthDate = userProfileRegister.getBirthDate();
        Profile profile = new Profile();
        profile.setUser(currentUser);
        profile.setGender(gender);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setBirthDate(birthDate);
        profileService.save(profile,bindingResult);
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
        User user1 = userService.findByEmail(user.getEmail());
        if (user1.getRegistrationType()== RegistrationType.OAUTH) return new ResponseEntity<>("Tài khoản này hiện chưa đăng ký",HttpStatus.BAD_REQUEST);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            // Nếu xác thực thành công
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Tạo ra token
            String jwt = jwtService.generateTokenLogin(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.findByEmail(user.getEmail());
            userService.setActive(currentUser, true);
            // Trả về phản hồi thành công với token
            return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), userDetails.getAuthorities()));
        }catch (BadCredentialsException e) {
            return new ResponseEntity<>("Sai mật khẩu hoăc password", HttpStatus.BAD_REQUEST);
        }catch (Throwable e) {
            return new ResponseEntity<>("Tài khoản chưa kích hoạt", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login/oauth")
    public ResponseEntity<?> loginWithOAuth(@Validated @RequestBody UserOAuth userOAuth, BindingResult bindingResult) {
        User user1 = userService.findByEmail(userOAuth.getEmail());
        if (user1 == null) {
            User user = new User();
            user.setEmail(userOAuth.getEmail());
            user.setPassword(passwordEncoder.encode(userOAuth.getIdentifier()));
            user.setConfirmPassword(passwordEncoder.encode(userOAuth.getIdentifier()));
            Role role = roleService.findByName("ROLE_USER");
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
            user.setEnabled(true);
            user.setRegistrationType(RegistrationType.OAUTH);
            userService.save(user);
            User currentUser = userService.findByEmail(user.getEmail());
            String firstName = userOAuth.getFirstName();
            String lastName = userOAuth.getLastName();
            Profile profile = new Profile();
            profile.setUser(currentUser);
            profile.setFirstName(firstName);
            profile.setLastName(lastName);
            profileService.save(profile,bindingResult);
        }
        String jwt = jwtService.generateTokenLoginOauth2(userOAuth.getEmail());
        User currentUser = userService.findByEmail(userOAuth.getEmail());
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(currentUser.getEmail())
                .password(currentUser.getPassword())
                .authorities("ROLE_USER")
                .build();
        userService.setActive(currentUser, true);
        return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), userDetails.getAuthorities()));
    }
    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(@RequestBody User user) {
        User currentUser = userService.findByEmail(user.getEmail());
        userService.setActive(currentUser, false);
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("Đăng xuất thành công", HttpStatus.OK);
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
        return new ResponseEntity("Hello", HttpStatus.OK);
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