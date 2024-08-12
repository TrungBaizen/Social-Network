package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {
    List<Profile> findAllByFirstNameContainingOrLastNameContaining(String firstname, String lastname);
}
