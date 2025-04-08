package com.mawulidev.week5labs.controllers;

import com.mawulidev.week5labs.dtos.ResponseHandler;
import com.mawulidev.week5labs.dtos.UserUpdateRequestDTO;
import com.mawulidev.week5labs.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")

@Secured("ADMIN")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @Secured("ADMIN")
    @GetMapping("/v1/users")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseHandler.successResponse(HttpStatus.OK, userService.getAllUsers());
    }

    @Secured("ADMIN")
    @GetMapping("/v1/users/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable String id) {
        return ResponseHandler.successResponse(HttpStatus.OK, userService.getUserById(id));
    }

    @Secured("ADMIN")
    @PutMapping("/v1/users/{id}")
    public ResponseEntity<Object> updateUserById(@PathVariable String id, @RequestBody UserUpdateRequestDTO user) {
        return ResponseHandler.successResponse(HttpStatus.OK, userService.updateUser(id, user));
    }

    @Secured("ADMIN")
    @DeleteMapping("/v1/users/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable String id) {
        return ResponseHandler.successResponse(HttpStatus.OK, userService.deleteUserById(id));
    }
}
