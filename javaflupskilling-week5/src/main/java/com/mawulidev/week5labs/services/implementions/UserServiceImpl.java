package com.mawulidev.week5labs.services.implementions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mawulidev.week5labs.dtos.UserDTO;
import com.mawulidev.week5labs.dtos.UserResponseDTO;
import com.mawulidev.week5labs.dtos.UserUpdateRequestDTO;
import com.mawulidev.week5labs.enums.Role;
import com.mawulidev.week5labs.exceptions.EntityNotFoundException;
import com.mawulidev.week5labs.exceptions.UserException;
import com.mawulidev.week5labs.models.User;
import com.mawulidev.week5labs.repositories.UserRepository;
import com.mawulidev.week5labs.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ObjectMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDTO> getAllUsers() {

        List<User> users = userRepository.findAll();
        return users.stream().map(user -> mapper.convertValue(user, UserResponseDTO.class)).toList();
    }

    @Override
    public String createUser(UserDTO userDTO) {
        userRepository.findUsersByEmail(userDTO.getEmail())
                .ifPresent(user -> {
                    throw new UserException("User with the email already exists");
                });

        // Convert DTO to User entity and set additional properties
        User newUser = mapper.convertValue(userDTO, User.class);
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setRole(Role.USER);

        userRepository.save(newUser);
        return "User successfully created";
    }

    @Override
    public UserResponseDTO updateUser(String id, UserUpdateRequestDTO userDTO) {
        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " does not exist."));

        user.setEmail(userDTO.getEmail());
        user.setUsername(user.getUsername());
        User updatedUser = userRepository.save(user);
        return mapper.convertValue(updatedUser, UserResponseDTO.class);
    }

    @Override
    public String deleteUserById(String id) {
        // Find user by ID and throw an exception if not found
        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " does not exist."));
        userRepository.delete(user);
        return "User with ID " + id + " has been successfully deleted.";
    }

    @Override
    public UserResponseDTO getUserById(String id) {
        // Find user by ID and throw an exception if not found
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " does not exist."));

        // Convert User entity to UserDTO (assuming you have a mapping method)
        return mapper.convertValue(user, UserResponseDTO.class);
    }
}
