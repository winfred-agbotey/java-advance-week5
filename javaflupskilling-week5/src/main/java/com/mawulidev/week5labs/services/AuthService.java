package com.mawulidev.week5labs.services;

import com.mawulidev.week5labs.dtos.AuthRequest;
import com.mawulidev.week5labs.dtos.AuthenticationResponse;
import com.mawulidev.week5labs.dtos.UserDTO;

public interface AuthService {
    AuthenticationResponse authenticate(AuthRequest authRequestDTO);
    AuthenticationResponse signup(UserDTO userDTO);
}
