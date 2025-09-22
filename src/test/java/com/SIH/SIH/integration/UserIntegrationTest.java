package com.SIH.SIH.integration;

import com.SIH.SIH.builder.UserTestDataBuilder;
import com.SIH.SIH.dto.UserDto;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto validUserDto;

    @BeforeEach
    void setUp() {
        // Clean up test data
        userRepository.deleteAll();
        
        // Create valid user DTO
        validUserDto = UserTestDataBuilder.createValidUserDto();
    }

    @Test
    void signup_WithValidData_ShouldCreateUser() throws Exception {
        // When & Then
        mockMvc.perform(post("/public/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.user").exists());

        // Verify user was saved in database
        assertEquals(1, userRepository.count());
        User savedUser = userRepository.findByEmail(validUserDto.getEmail());
        assertEquals(validUserDto.getFirstName(), savedUser.getFirstName());
        assertEquals(validUserDto.getLastName(), savedUser.getLastName());
    }

    @Test
    void signup_WithDuplicateEmail_ShouldReturnConflict() throws Exception {
        // Given - Create user first
        userRepository.save(UserTestDataBuilder.createValidUser());

        // When & Then
        mockMvc.perform(post("/public/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("User with email " + validUserDto.getEmail() + " already exists"));
    }

    @Test
    void signup_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        UserDto invalidUserDto = UserTestDataBuilder.createUserDto();
        invalidUserDto.setEmail("invalid-email");
        invalidUserDto.setPassword("weak");
        invalidUserDto.setPhoneNumber("123"); // Invalid phone number

        // When & Then
        mockMvc.perform(post("/public/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() throws Exception {
        // Given - Create user first
        User user = UserTestDataBuilder.createValidUser();
        userRepository.save(user);

        UserDto loginDto = UserTestDataBuilder.createUserDto();
        loginDto.setPassword("Test@123");

        // When & Then
        mockMvc.perform(post("/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user").exists());
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Given
        UserDto loginDto = UserTestDataBuilder.createUserDto();
        loginDto.setPassword("wrong-password");

        // When & Then
        mockMvc.perform(post("/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Authentication Failed"));
    }

    @Test
    @WithMockUser(username = "john.doe@example.com")
    void updateUserProfile_WithValidData_ShouldUpdateProfile() throws Exception {
        // Given - Create user first
        User user = UserTestDataBuilder.createValidUser();
        user = userRepository.save(user);

        UserDto updateDto = UserTestDataBuilder.createUserDto();
        updateDto.setFirstName("Updated John");
        updateDto.setLastName("Updated Doe");
        updateDto.setCity("Delhi");

        // When & Then
        mockMvc.perform(put("/user/update/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated John"))
                .andExpect(jsonPath("$.lastName").value("Updated Doe"))
                .andExpect(jsonPath("$.city").value("Delhi"));
    }

    @Test
    @WithMockUser(username = "john.doe@example.com")
    void deleteUser_ShouldDeleteUser() throws Exception {
        // Given - Create user first
        User user = UserTestDataBuilder.createValidUser();
        user = userRepository.save(user);

        // When & Then
        mockMvc.perform(delete("/user/delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User account deleted successfully"));

        // Verify user was deleted from database
        assertEquals(0, userRepository.count());
    }
}
