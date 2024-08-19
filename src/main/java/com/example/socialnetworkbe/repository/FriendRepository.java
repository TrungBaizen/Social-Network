package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.Friend;
import com.example.socialnetworkbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

}
