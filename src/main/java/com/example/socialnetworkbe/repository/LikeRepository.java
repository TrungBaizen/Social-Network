package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
}
