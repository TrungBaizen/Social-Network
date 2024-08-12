package com.example.socialnetworkbe.service.impl;

import com.example.socialnetworkbe.model.User;
import com.example.socialnetworkbe.model.UserPrinciple;
import com.example.socialnetworkbe.repository.UserRepository;
import com.example.socialnetworkbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 6; // độ dài mật khẩu mới

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
    @Override
    @Transactional
    //Tạo ra 1 user details (tương tác với ứng dụng mỗi lần đăng nhập)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        if (this.checkLogin(user)) {
            return UserPrinciple.build(user);
        }
        boolean enable = false;
        boolean accountNonExpired = false;
        boolean credentialsNonExpired = false;
        boolean accountNonLocked = false;
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), enable, accountNonExpired, credentialsNonExpired,
                accountNonLocked, null);
    }
    @Override
    public  String resetPassword(String to) {
        String newPassword = generateNewPassword();
        emailService.sendEmail(to,"Cấp mật khẩu mới","Mật khẩu mới là: " + newPassword + "\nVui lòng thay đổi mật khẩu mới khi đăng nhập.");

        return newPassword;
    }
    private String generateNewPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public User save(User user) {
        user.setVerificationToken(generateVerificationToken());
        User savedUser = userRepository.save(user);

        // Gửi email xác thực
        String verificationLink = "http://localhost:3000/successnotification?token=" + user.getVerificationToken();

        emailService.sendEmail(
                user.getEmail(),
                "Xác thực tài khoản của bạn",
                "Vui lòng xác thực tài khoản của bạn bằng cách nhấp vào liên kết sau: " + verificationLink
        );

        return savedUser;
    }
    private String generateVerificationToken() {
        // Tạo mã xác thực duy nhất
        return UUID.randomUUID().toString();
    }
    @Override
    public User findByVerificationToken(String token) {
        return userRepository.findByVerificationToken(token);
    }
    @Override
    public void verifyUser(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    // lấy ra user đang đăng nhập hiện tại
    public User getCurrentUser() {
        User user;
        String email;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        user = this.findByEmail(email);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NullPointerException();
        }
        return UserPrinciple.build(user.get());
    }

    @Override
    // kiểm tra đăng nhập
    public boolean checkLogin(User user) {
        Iterable<User> users = this.findAll();
        boolean isCorrectUser = false;
        for (User currentUser : users) {
            if (currentUser.getEmail().equals(user.getEmail()) && user.getPassword().equals(currentUser.getPassword()) && currentUser.isEnabled()) {
                isCorrectUser = true;
                break;
            }
        }
        return isCorrectUser;
    }

    @Override
    public boolean isRegister(User user) {
        boolean isRegister = false;
        Iterable<User> users = this.findAll();
        for (User currentUser : users) {
            if (user.getEmail().equals(currentUser.getEmail())) {
                isRegister = true;
                break;
            }
        }
        return isRegister;
    }
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    @Override
    public boolean isCorrectConfirmPassword(User user) {
        return user.getPassword().equals(user.getConfirmPassword());
    }

    @Override
    public void setActive(User user , boolean isActive) {
        user.setActive(isActive);
        update(user);
    }
}
