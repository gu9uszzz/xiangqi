package com.example.springboot.service;

import com.example.springboot.exception.ServiceException; // Assuming you have this
import com.example.springboot.entity.VerificationCode;
import com.example.springboot.mapper.VerificationCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Random;
@Service
public class VerificationCodeService {
    @Autowired
    private VerificationCodeMapper verificationCodeMapper;
    private static final int CODE_LENGTH = 6; // e.g., 6-digit code
    private static final int EXPIRATION_MINUTES = 5; // e.g., 5 minutes validity
    // Generates a random numeric code
    private String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    // Creates, stores, and returns a new code for the email
    @Transactional // Ensure atomicity
    public String createAndStoreCode(String email) {
        // Optional: Delete previous codes for the same email to prevent clutter
        // verificationCodeMapper.deleteByEmail(email);
        String code = generateCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
        VerificationCode vc = new VerificationCode();
        vc.setEmail(email);
        vc.setCode(code);
        vc.setExpiresAt(expiresAt);
        verificationCodeMapper.insert(vc);
        return code;
    }
    // Validates the provided code against the stored one
    public boolean validateCode(String email, String code) {
        if (email == null || code == null) {
            return false;
        }
        VerificationCode foundCode = verificationCodeMapper.findLatestValidCode(email, LocalDateTime.now());
        return foundCode != null && foundCode.getCode().equals(code);
    }
    // Deletes the used code after successful validation
    @Transactional
    public void deleteUsedCode(String email, String code) {
        verificationCodeMapper.deleteByEmailAndCode(email, code);
    }
    // Method for scheduled cleanup task
    @Transactional
    public int cleanupExpiredCodes() {
        return verificationCodeMapper.deleteExpiredCodes(LocalDateTime.now());
        // log.info("Cleaned up {} expired verification codes.", count);
    }
}
