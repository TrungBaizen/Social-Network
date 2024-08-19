package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.FriendRequest;

import java.util.List;
import java.util.Optional;

public interface FriendRequestService {
    FriendRequest sendFriendRequest(Long senderId, Long receiverId);

    FriendRequest acceptFriendRequest(Long requestId);

    FriendRequest rejectFriendRequest(Long requestId);

    List<FriendRequest> getPendingRequestsForUser(Long userId);

    Optional<FriendRequest> getRequest(Long senderId, Long receiverId);

    void cancelFriendRequest(Long senderId, Long receiverId);
}
