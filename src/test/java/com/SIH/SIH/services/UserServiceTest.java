package com.SIH.SIH.services;

import com.SIH.SIH.builder.UserTestDataBuilder;
import com.SIH.SIH.dto.UserDto;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDto validUserDto;
    private User validUser;

    @BeforeEach
    void setUp() {
        validUserDto = UserTestDataBuilder.createValidUserDto();
        validUser = UserTestDataBuilder.createValidUser();
    }

    @Test
    void saveUser_WithValidData_ShouldSaveUser() {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        // When
        User result = userService.saveUser(validUserDto);

        // Then
        assertNotNull(result);
        assertEquals(validUserDto.getFirstName(), result.getFirstName());
        assertEquals(validUserDto.getLastName(), result.getLastName());
        assertEquals(validUserDto.getEmail(), result.getEmail());
        assertEquals(validUserDto.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(validUserDto.getRole(), result.getRole());
        
        verify(passwordEncoder).encode(validUserDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void saveUser_WithStaffData_ShouldSaveStaffUser() {
        // Given
        UserDto staffDto = UserTestDataBuilder.createStaffDto();
        User staffUser = UserTestDataBuilder.createStaffUser();
        
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(staffUser);

        // When
        User result = userService.saveUser(staffDto);

        // Then
        assertNotNull(result);
        assertEquals(staffDto.getDepartment(), result.getDepartment());
        assertEquals(staffDto.getDesignation(), result.getDesignation());
        assertEquals(staffDto.getRole(), result.getRole());
    }

    @Test
    void saveUserPassword_WithValidUser_ShouldUpdatePassword() {
        // Given
        String newPassword = "NewPassword@123";
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        // When
        userService.saveUserPassword(validUser,newPassword);

        // Then
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(validUser);
    }

    @Test
    void saveUser_WithNullData_ShouldHandleGracefully() {
        // Given
        UserDto nullDto = new UserDto(); // all fields null

        // When & Then
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            userService.saveUser(nullDto);
        });

        // Optionally check the message
        assertTrue(exception.getMessage().contains("firstName"));
    }

    @Test
    void saveUserPassword_WithNullPassword_ShouldHandleGracefully() {
        // Given
        String newPassword = null;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUserPassword(validUser,newPassword);
        });
        assertEquals("Password cannot be null", exception.getMessage());

    }
}
