package com.mawulidev.week5labs.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String description;

}