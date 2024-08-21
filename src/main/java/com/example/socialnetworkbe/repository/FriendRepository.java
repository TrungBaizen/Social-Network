package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.Friend;
import com.example.socialnetworkbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("SELECT f FROM Friend f WHERE f.user.email = :email OR f.friendUser.email = :email")

    List<Friend> findAllByEmail(String email);

    Optional<Friend> findByUserAndFriendUser(User user, User friendUser);

    boolean existsByUserAndFriendUser(User user, User friendUser);

    Optional<Friend> findByUserIdAndFriendUserId(Long userId, Long friendId);

    List<Friend> findByUserId(Long userId);

    List<Friend> findByUser(User user);


    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Friend f WHERE f.user.id = :senderId AND f.friendUser.id = :receiverId")
    boolean existsBySenderIdAndReceiverId(@Param("senderId") Long senderId,
                                          @Param("receiverId") Long receiverId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Friend f WHERE f.user.id = :senderId AND f.friendUser.id = :receiverId AND f.isAccepted = TRUE")
    boolean areFriends(@Param("senderId") Long senderId,
                       @Param("receiverId") Long receiverId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Friend f WHERE f.user.id = :userId AND f.friendUser.id = :friendUserId AND f.isFollow = TRUE")
    boolean isFollowing(@Param("userId") Long userId,
                        @Param("friendUserId") Long friendUserId);
}
