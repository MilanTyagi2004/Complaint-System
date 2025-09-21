package com.SIH.SIH.controller;

import com.SIH.SIH.entity.Role;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.UserRepository;
import com.SIH.SIH.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name ="Admin APIs",description = "Get all-User,Create new Admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/AllStaff")
    public ResponseEntity<?> getallStaff(){
        try{
            Role staffRole = Role.STAFF;
            List<User> userList = userRepository.findByRole(staffRole);
            return  new ResponseEntity<>(userList,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/delete/staff/{staffId}")
    public ResponseEntity<?> deleteStaff(@PathVariable String staffId ){
        try{
            userRepository.deleteById(staffId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return  new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
}
