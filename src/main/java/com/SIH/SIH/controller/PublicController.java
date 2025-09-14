package com.SIH.SIH.controller;

import com.SIH.SIH.dto.UserDto;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.UserRepository;
import com.SIH.SIH.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository  userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto user){
        try{
            User user1 = new User();
            user1.setFirstName(user.getFirstName());
            user1.setLastName(user.getLastName());
            user1.setEmail(user.getEmail());
            user1.setPassword(user.getPassword());
            user1.setMobileNumber(user.getMobileNumber());
            userService.saveUser(user1);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody UserDto user){
        try{
            User existingUser = userRepository.findByEmail(user.getEmail());
            if(existingUser==null){
                return new ResponseEntity<>("user not found",HttpStatus.NOT_FOUND);
            }
            if (!existingUser.getPassword().equals(user.getPassword())) {
                return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return  new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
