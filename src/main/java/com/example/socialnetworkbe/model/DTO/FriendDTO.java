package com.example.socialnetworkbe.model.DTO;

import com.example.socialnetworkbe.model.Friend;
import com.example.socialnetworkbe.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FriendDTO {
    private Long userId;
    private String email;
    private String imageAvatar;
    private String firstName;
    private String lastName;

    public FriendDTO(Friend friend, Profile friendProfile){
        this.userId = friend.getFriendUser().getId();
        this.email =friend.getUser().getEmail();
        this.imageAvatar = friendProfile.getImageAvatar();
        this.firstName = friendProfile.getFirstName();
        this.lastName = friendProfile.getLastName();
    }
}
