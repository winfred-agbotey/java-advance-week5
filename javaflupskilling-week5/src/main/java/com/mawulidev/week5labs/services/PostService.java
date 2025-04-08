package com.mawulidev.week5labs.services;

import com.mawulidev.week5labs.dtos.PostRequest;
import com.mawulidev.week5labs.models.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post createPost(PostRequest request);

    List<Post> getAllPosts();
    List<Post> getUserPosts(UUID userId);
    void deletePost(UUID postId);

}
