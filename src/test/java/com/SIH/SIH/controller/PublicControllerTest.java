package com.SIH.SIH.controller;

import com.SIH.SIH.builder.UserTestDataBuilder;
import com.SIH.SIH.dto.UserDto;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.UserRepository;
import com.SIH.SIH.services.UserService;
import com.SIH.SIH.services.UserDetailServiceImpl;
import com.SIH.SIH.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto validUserDto;
    private User validUser;

    @BeforeEach
    void setUp() {
        validUserDto = UserTestDataBuilder.createValidUserDto();
        validUser = UserTestDataBuilder.createValidUser();
        validUser.setId("test-user-id"); // Set an ID for the user
    }

    @Test
    void signup_WithValidData_ShouldReturnCreated() throws Exception {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userService.saveUser(any(UserDto.class))).thenReturn(validUser);
        // When & Then
        mockMvc.perform(post("/public/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.user").exists());
    }

    @Test
    void signup_WithExistingEmail_ShouldReturnConflict() throws Exception {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(validUser);

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

        // When & Then
        mockMvc.perform(post("/public/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() throws Exception {
        // Given
        UserDto loginDto = UserTestDataBuilder.createUserDto();
        loginDto.setPassword("Test@123");
        
        when(userRepository.findByEmail(anyString())).thenReturn(validUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("test-jwt-token");

        // Mock successful authentication
        when(authenticationManager.authenticate(any())).thenReturn(null);

        // When & Then
        mockMvc.perform(post("/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.token").value("test-jwt-token"))
                .andExpect(jsonPath("$.user").exists());
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Given
        UserDto loginDto = UserTestDataBuilder.createUserDto();
        loginDto.setPassword("wrong-password");

        // Mock authentication failure
        when(authenticationManager.authenticate(any()))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        // When & Then
        mockMvc.perform(post("/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }
}
