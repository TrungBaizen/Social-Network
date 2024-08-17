package com.example.socialnetworkbe.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FriendRequestDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private boolean isFollow;
    private boolean isAccepted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
