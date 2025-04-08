package com.mawulidev.week5labs.repositories;

import com.mawulidev.week5labs.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
