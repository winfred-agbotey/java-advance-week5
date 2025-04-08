package com.mawulidev.week5labs.controllers;

import com.mawulidev.week5labs.dtos.PostRequest;
import com.mawulidev.week5labs.dtos.ResponseHandler;
import com.mawulidev.week5labs.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @Secured("USER")
    public ResponseEntity<Object> createPost(@RequestBody PostRequest request) {
        return ResponseHandler.successResponse(HttpStatus.OK, postService.createPost(request));
    }

    @GetMapping
    @Secured("USER")
    public ResponseEntity<Object> getAllPosts() {
        return ResponseHandler.successResponse(HttpStatus.OK, postService.getAllPosts());
    }

    @GetMapping("/{userId}")
    @Secured("USER")
    public ResponseEntity<Object> getUserPosts(@PathVariable UUID userId) {
        return ResponseHandler.successResponse(HttpStatus.OK, postService.getUserPosts(userId));
    }

    @DeleteMapping("/{postId}")
    @Secured("USER")
    public ResponseEntity<Void> deletePost(@PathVariable UUID postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }
}