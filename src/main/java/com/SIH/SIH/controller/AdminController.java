package com.SIH.SIH.controller;

import com.SIH.SIH.entity.Role;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.exception.ResourceNotFoundException;
import com.SIH.SIH.repostitory.UserRepository;
import com.SIH.SIH.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Tag(name ="Admin APIs",description = "Administrative endpoints for user and staff management")
public class AdminController {
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/staff")
    @Operation(summary = "Get all staff members", description = "Retrieve all users with STAFF role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Staff list retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "403", description = "Access denied - Admin role required")
    })
    public ResponseEntity<List<User>> getAllStaff() {
        Role staffRole = Role.STAFF;
        List<User> staffList = userRepository.findByRole(staffRole);
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }
    
    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Retrieve all users with USER role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users list retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "403", description = "Access denied - Admin role required")
    })
    public ResponseEntity<List<User>> getAllUsers() {
        Role userRole = Role.USER;
        List<User> userList = userRepository.findByRole(userRole);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
    
    @DeleteMapping("/staff/{staffId}")
    @Operation(summary = "Delete staff member", description = "Delete a staff member by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Staff member deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Staff member not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "403", description = "Access denied - Admin role required")
    })
    public ResponseEntity<Map<String, String>> deleteStaff(@PathVariable String staffId) {
        User staff = userRepository.findById(staffId).orElse(null);
        if (staff == null) {
            throw new ResourceNotFoundException("Staff", "id", staffId);
        }
        
        if (staff.getRole() != Role.STAFF) {
            throw new ResourceNotFoundException("Staff member", "id", staffId);
        }
        
        userRepository.deleteById(staffId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Staff member deleted successfully");
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/users/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "403", description = "Access denied - Admin role required")
    })
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
