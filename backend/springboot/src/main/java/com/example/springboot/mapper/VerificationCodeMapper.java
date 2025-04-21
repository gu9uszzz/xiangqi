package com.example.springboot.mapper; // Adjust package name if needed

import com.example.springboot.entity.VerificationCode;
import org.apache.ibatis.annotations.*; // Import necessary annotations
import java.time.LocalDateTime;

@Mapper // Mark this as a MyBatis Mapper interface
public interface VerificationCodeMapper {

    // Insert a new verification code record
    @Insert("INSERT INTO verification_codes (email, code, expires_at, created_at) " +
            "VALUES (#{email}, #{code}, #{expiresAt}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id") // Get auto-generated ID back if needed
    int insert(VerificationCode verificationCode);

    // Find the latest valid code for an email (expires_at > now)
    @Select("SELECT id, email, code, expires_at, created_at " +
            "FROM verification_codes " +
            "WHERE email = #{email} AND expires_at > #{now} " +
            "ORDER BY created_at DESC " +
            "LIMIT 1")
    VerificationCode findLatestValidCode(@Param("email") String email, @Param("now") LocalDateTime now);

    // Delete a specific code after successful verification
    @Delete("DELETE FROM verification_codes " +
            "WHERE email = #{email} AND code = #{code}")
    int deleteByEmailAndCode(@Param("email") String email, @Param("code") String code);

    // Delete all codes that have expired (for cleanup)
    @Delete("DELETE FROM verification_codes " +
            "WHERE expires_at <= #{now}")
    int deleteExpiredCodes(@Param("now") LocalDateTime now);

    // (Optional) Delete all codes for a specific email
    @Delete("DELETE FROM verification_codes WHERE email = #{email}")
    int deleteByEmail(@Param("email") String email);
}
