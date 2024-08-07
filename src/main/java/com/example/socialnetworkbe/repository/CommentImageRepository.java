package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.CommentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentImageRepository extends JpaRepository<CommentImage,Long> {
}
