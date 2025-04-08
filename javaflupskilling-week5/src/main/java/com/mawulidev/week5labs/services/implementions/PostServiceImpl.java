package com.mawulidev.week5labs.services.implementions;

import com.mawulidev.week5labs.dtos.PostRequest;
import com.mawulidev.week5labs.models.Post;
import com.mawulidev.week5labs.models.User;
import com.mawulidev.week5labs.repositories.PostRepository;
import com.mawulidev.week5labs.repositories.UserRepository;
import com.mawulidev.week5labs.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Post createPost(PostRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUsersByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setContent(request.getContent());
        post.setUser(user);

        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getUserPosts(UUID userId) {
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public void deletePost(UUID postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only delete your own posts");
        }

        postRepository.delete(post);
    }
}
