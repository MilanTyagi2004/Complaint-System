package com.SIH.SIH.controller;

import com.SIH.SIH.dto.UserDto;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.exception.ResourceNotFoundException;
import com.SIH.SIH.exception.UnauthorizedException;
import com.SIH.SIH.repostitory.UserRepository;
import com.SIH.SIH.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name ="User APIs",description = "User profile management endpoints")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @DeleteMapping("/delete")
    @Operation(summary = "Delete user account", description = "Delete the authenticated user's account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Map<String, String>> deleteByEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            throw new UnauthorizedException("User not authenticated");
        }
        
        User user = userRepository.findByEmail(authentication.getName());
        if (user == null) {
            throw new ResourceNotFoundException("User", "email", authentication.getName());
        }
        
        userRepository.deleteByEmail(authentication.getName());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User account deleted successfully");
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/profile")
    @Operation(summary = "Update user profile", description = "Update user's personal information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            throw new UnauthorizedException("User not authenticated");
        }

        String email = authentication.getName();
        User userInDb = userRepository.findByEmail(email);
        if (userInDb == null) {
            throw new ResourceNotFoundException("User", "email", email);
        }
        
        userInDb.setFirstName(userDto.getFirstName());
        userInDb.setLastName(userDto.getLastName());
        userInDb.setEmail(userDto.getEmail());
        userInDb.setPhoneNumber(userDto.getPhoneNumber());
        userInDb.setCity(userDto.getCity());

        User updatedUser = userRepository.save(userInDb);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    
    @PutMapping("/update/password")
    @Operation(summary = "Update password", description = "Update user's password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid password format"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            throw new UnauthorizedException("User not authenticated");
        }
        
        String email = authentication.getName();
        User userInDb = userRepository.findByEmail(email);

        if (userInDb == null) {
            throw new ResourceNotFoundException("User", "email", email);
        }
        // Validate input password first
        String newPassword = userDto.getPassword();
        if (newPassword == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        // Let the service handle encoding and saving
        userService.saveUserPassword(userInDb, newPassword);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password updated successfully");
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
