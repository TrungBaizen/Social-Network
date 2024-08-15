package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {
    @Query(value = "select f.* from friends f join tdh.user u on u.id = f.friend_user_id where u.email = :email",nativeQuery = true)
    List<Friend> findAllByEmail(String email);
}
