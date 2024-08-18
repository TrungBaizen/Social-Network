package com.example.socialnetworkbe.model.DTO;

import com.example.socialnetworkbe.enums.Gender;
import com.example.socialnetworkbe.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileDTO {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String description;
    private LocalDate birthDate;
    private String hometown;
    private String currentLocation;
    private String occupation;
    private String imageAvatar;
    private String imageCover;
    private List<FriendDTO> friendList;


    public ProfileDTO(Profile profile,List<FriendDTO> friendDTOList){
        this.userId = profile.getUser().getId();
        this.email = profile.getUser().getEmail();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.gender = profile.getGender();
        this.description = profile.getDescription();
        this.birthDate = profile.getBirthDate();
        this.hometown = profile.getHometown();
        this.currentLocation = profile.getCurrentLocation();
        this.occupation = profile.getOccupation();
        this.imageAvatar = profile.getImageAvatar();
        this.imageCover = profile.getImageCover();
        this.friendList = friendDTOList;
    }
}
