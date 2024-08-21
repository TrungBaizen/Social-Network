package com.example.socialnetworkbe.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class FriendRequestDTO {
    private Long id;
    private Long senderId;
    private String senderEmail;
    private String senderFirstName;
    private String senderLastName;
    private Long receiverId;
    private String receiverEmail;
    private String receiverFirstName;
    private String receiverLastName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private boolean follow;
    private boolean accepted;


    // Constructor với tất cả các trường
    public FriendRequestDTO(Long id, Long senderId, String senderEmail, String senderFirstName, String senderLastName,
                            Long receiverId, String receiverEmail, String receiverFirstName, String receiverLastName,
                            LocalDateTime createTime, LocalDateTime updateTime, boolean follow, boolean accepted) {
        this.id = id;
        this.senderId = senderId;
        this.senderEmail = senderEmail;
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
        this.receiverId = receiverId;
        this.receiverEmail = receiverEmail;
        this.receiverFirstName = receiverFirstName;
        this.receiverLastName = receiverLastName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.follow = follow;
        this.accepted = accepted;
    }


}
