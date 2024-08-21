package com.example.socialnetworkbe.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FriendshipStatusDTO {
    private boolean isFriend;
    private boolean isFollowing;
    private boolean isRequestSent;

}

