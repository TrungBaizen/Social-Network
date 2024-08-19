package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.Follow;
import com.example.socialnetworkbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    Optional<Follow> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);

}
