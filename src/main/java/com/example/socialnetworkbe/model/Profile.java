package com.example.socialnetworkbe.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String firstName;
    private String lastName;
    @Column(columnDefinition = "LONGTEXT")
    private String avatar;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
