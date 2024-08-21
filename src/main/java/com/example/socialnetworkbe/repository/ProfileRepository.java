package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {
    List<Profile> findAllByFirstNameContainingOrLastNameContaining(String firstname, String lastname);
    Profile findByUserId(Long userId);
    @Query(value = "SELECT profiles.* FROM profiles join tdh.user u on u.id = profiles.user_id where u.email = :email",nativeQuery = true)
    Profile findByEmail(String email);
}
