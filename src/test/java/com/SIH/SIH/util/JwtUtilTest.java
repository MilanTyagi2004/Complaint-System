package com.SIH.SIH.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private final String testSecret = "test-secret-key-for-testing-purposes-only";
    private final String testUsername = "test@example.com";
    private final String testRole = "USER";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "SECRET_KEY", testSecret);
    }

    @Test
    void generateToken_WithValidData_ShouldGenerateToken() {
        // When
        String token = jwtUtil.generateToken(testUsername, testRole);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains(".")); // JWT format has dots
    }

    @Test
    void extractUsername_WithValidToken_ShouldReturnUsername() {
        // Given
        String token = jwtUtil.generateToken(testUsername, testRole);

        // When
        String extractedUsername = jwtUtil.extractUsername(token);

        // Then
        assertEquals(testUsername, extractedUsername);
    }

    @Test
    void extractRole_WithValidToken_ShouldReturnRole() {
        // Given
        String token = jwtUtil.generateToken(testUsername, testRole);

        // When
        String extractedRole = jwtUtil.extractRole(token);

        // Then
        assertEquals(testRole, extractedRole);
    }

    @Test
    void extractExpiration_WithValidToken_ShouldReturnExpiration() {
        // Given
        String token = jwtUtil.generateToken(testUsername, testRole);

        // When
        Date expiration = jwtUtil.extractExpiration(token);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date())); // Should be in the future
    }

    @Test
    void isTokenExpired_WithValidToken_ShouldReturnFalse() {
        // Given
        String token = jwtUtil.generateToken(testUsername, testRole);

        // When
        boolean isExpired = jwtUtil.isTokenExpired(token);

        // Then
        assertFalse(isExpired);
    }

    @Test
    void validateToken_WithValidTokenAndUsername_ShouldReturnTrue() {
        // Given
        String token = jwtUtil.generateToken(testUsername, testRole);

        // When
        boolean isValid = jwtUtil.validateToken(token, testUsername);

        // Then
        assertTrue(isValid);
    }

    @Test
    void validateToken_WithValidTokenButWrongUsername_ShouldReturnFalse() {
        // Given
        String token = jwtUtil.generateToken(testUsername, testRole);
        String wrongUsername = "wrong@example.com";

        // When
        boolean isValid = jwtUtil.validateToken(token, wrongUsername);

        // Then
        assertFalse(isValid);
    }

    @Test
    void createToken_WithCustomClaims_ShouldIncludeClaims() {
        // Given
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("role", testRole);
        customClaims.put("department", "IT");
        customClaims.put("userId", "123");

        // When
        String token = jwtUtil.createToken(customClaims, testUsername);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verify we can extract the username
        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(testUsername, extractedUsername);
    }

    @Test
    void extractAllClaims_WithValidToken_ShouldReturnClaims() {
        // Given
        String token = jwtUtil.generateToken(testUsername, testRole);

        // When
        var claims = jwtUtil.extractAllClaims(token);

        // Then
        assertNotNull(claims);
        assertEquals(testUsername, claims.getSubject());
        assertEquals(testRole, claims.get("role"));
    }

    @Test
    void generateToken_WithDifferentRoles_ShouldGenerateDifferentTokens() {
        // Given
        String userToken = jwtUtil.generateToken(testUsername, "USER");
        String adminToken = jwtUtil.generateToken(testUsername, "ADMIN");

        // When & Then
        assertNotEquals(userToken, adminToken);
        
        // Verify roles are different
        assertEquals("USER", jwtUtil.extractRole(userToken));
        assertEquals("ADMIN", jwtUtil.extractRole(adminToken));
    }

    @Test
    void generateToken_WithEmptyUsername_ShouldGenerateToken() {
        // When
        String token = jwtUtil.generateToken("", testRole);
        
        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        // Empty username should still generate a valid token
        // Note: JWT library may return null for empty subject
        String extractedUsername = jwtUtil.extractUsername(token);
        assertTrue(extractedUsername == null || extractedUsername.isEmpty());
    }

    @Test
    void generateToken_WithNullRole_ShouldGenerateToken() {
        // When
        String token = jwtUtil.generateToken(testUsername, null);

        // Then
        assertNotNull(token);
        assertNull(jwtUtil.extractRole(token));
    }
}
