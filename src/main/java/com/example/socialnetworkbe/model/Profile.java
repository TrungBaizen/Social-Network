package com.example.socialnetworkbe.model;


import com.example.socialnetworkbe.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Past(message = "Ngày sinh phải ngày trong quá khứ")
    private LocalDate birthDate;
    @Column(name = "hometown")
    private String hometown;
    @Column(name = "current_location")
    private String currentLocation;
    @Column(name = "occupation")
    private String occupation;
    @Column(columnDefinition = "LONGTEXT")
    private String imageAvatar;
    @Column(columnDefinition = "LONGTEXT")
    private String imageCover;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
