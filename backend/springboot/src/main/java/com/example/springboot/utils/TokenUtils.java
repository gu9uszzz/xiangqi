package utils; // Adjust package name

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springboot.entity.User; // Assuming User entity has id, username, email
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct; // Make sure javax.annotation-api dependency is present
import java.util.Date;

@Component
public class TokenUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration.ms}")
    private long expirationMs;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    // Initialize algorithm and verifier after properties are injected
    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secretKey); // Use HMAC256 with your secret
        this.verifier = JWT.require(algorithm).build(); // Reusable verifier instance
    }

    /**
     * Generates a JWT for the given user.
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return JWT.create()
                .withSubject(user.getUsername()) // Use username as subject
                .withClaim("userId", user.getId()) // Add user ID as a claim
                .withClaim("email", user.getEmail()) // Add email as a claim
                // Add other claims as needed (e.g., roles)
                .withIssuedAt(now) // Token issue time
                .withExpiresAt(expiryDate) // Token expiry time
                .sign(algorithm); // Sign with the chosen algorithm
    }

    /**
     * Validates the token and returns the decoded JWT if valid.
     * Throws JWTVerificationException if invalid.
     */
    public DecodedJWT validateToken(String token) throws JWTVerificationException {
        return verifier.verify(token); // Use the pre-built verifier
    }

    /**
     * Extracts the username (subject) from a validated token.
     */
    public String getUsernameFromToken(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    /**
     * Extracts the user ID from a validated token.
     */
    public Integer getUserIdFromToken(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("userId").asInt();
    }
}
