package com.SIH.SIH.controller;

import com.SIH.SIH.dto.UserDto;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.exception.ConflictException;
import com.SIH.SIH.exception.ResourceNotFoundException;
import com.SIH.SIH.repostitory.UserRepository;
import com.SIH.SIH.services.UserService;
import com.SIH.SIH.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public")
@Tag(name ="Public APIs",description = "Authentication endpoints for user registration and login")
public class PublicController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    @Operation(summary = "User registration", description = "Register a new user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody UserDto userDto) {
        // Check if user already exists
        User existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser != null) {
            throw new ConflictException("User with email " + userDto.getEmail() + " already exists");
        }
        
        User user = userService.saveUser(userDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("user", user);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Map<String, Object>> signIn(@Valid @RequestBody UserDto user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        
        User userInDb = userRepository.findByEmail(user.getEmail());
        if (userInDb == null) {
            throw new ResourceNotFoundException("User", "email", user.getEmail());
        }
        
        String token = jwtUtil.generateToken(user.getEmail(), userInDb.getRole().name());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("token", token);
        response.put("user", Map.of(
            "id", userInDb.getId(),
            "email", userInDb.getEmail(),
            "role", userInDb.getRole(),
            "firstName", userInDb.getFirstName(),
            "lastName", userInDb.getLastName()
        ));
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}