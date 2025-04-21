package com.example.springboot.entity;

import lombok.Data;
import java.time.LocalDateTime; // Use LocalDateTime for better Java handling

@Data // Lombok annotation for getters, setters, toString, etc.
public class VerificationCode {
    private Integer id;
    private String email;
    private String code;
    private LocalDateTime expiresAt; // Map to DATETIME in DB
    private LocalDateTime createdAt; // Map to DATETIME in DB
}