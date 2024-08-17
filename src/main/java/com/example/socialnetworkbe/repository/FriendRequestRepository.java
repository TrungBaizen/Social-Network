package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    // Tìm yêu cầu kết bạn bằng ID của người gửi và người nhận
    Optional<FriendRequest> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    // Tìm tất cả yêu cầu kết bạn của người nhận với trạng thái nhất định
    List<FriendRequest> findByReceiverId(Long receiverId);
}
