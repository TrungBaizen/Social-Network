package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage,Long> {
}
