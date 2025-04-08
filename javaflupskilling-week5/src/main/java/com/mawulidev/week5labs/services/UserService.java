package com.mawulidev.week5labs.services;

import com.mawulidev.week5labs.dtos.UserDTO;
import com.mawulidev.week5labs.dtos.UserResponseDTO;
import com.mawulidev.week5labs.dtos.UserUpdateRequestDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();

    String createUser(UserDTO userDTO);

    UserResponseDTO updateUser(String id, UserUpdateRequestDTO userUpdateRequestDTO);
    String deleteUserById(String id);
    UserResponseDTO getUserById(String id);
}
