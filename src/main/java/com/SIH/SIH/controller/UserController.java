package com.SIH.SIH.controller;

import com.SIH.SIH.dto.UserDto;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.UserRepository;
import com.SIH.SIH.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name ="User APIs",description = "Delete User,update user email and password")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteByEmail(){

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication.getName().equals("anonymousUser")) {
                return new ResponseEntity<>("User Not authenticate", HttpStatus.UNAUTHORIZED);

            }
            userRepository.deleteByEmail(authentication.getName());
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/email")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName().equals("anonymousUser")){
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }

        String email = authentication.getName();
        User userIndb = userRepository.findByEmail(email);
        if(userIndb==null){
            return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
        }
        userIndb.setFirstName(userDto.getFirstName());
        userIndb.setLastName(userDto.getLastName());
        userIndb.setEmail(userDto.getEmail());

        userRepository.save(userIndb);
        return new ResponseEntity<>(userIndb, HttpStatus.OK);
    }
    @PutMapping("update/password")
    public ResponseEntity<?> updatePassword(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getName().equals("anonymousUser")){
            return new ResponseEntity<>("User not authenticated",HttpStatus.UNAUTHORIZED);
        }
        String email = authentication.getName();
        User userInDb = userRepository.findByEmail(email);

        if(userInDb ==null){
            return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
        }
        userInDb.setPassword(user.getPassword());
        userService.saveUserPassword(userInDb);
        return new ResponseEntity<>(userInDb,HttpStatus.OK);
    }
}
