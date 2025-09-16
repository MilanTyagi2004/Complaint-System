package com.SIH.SIH.controller;

import com.SIH.SIH.dto.StaffDto;
import com.SIH.SIH.entity.Staff;
import com.SIH.SIH.services.StaffService;
import com.SIH.SIH.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private StaffService staffService;
    @Autowired
    @Qualifier("staffAuthenticationManager")
    private AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody StaffDto staffDto){
        try{
            Staff staff = new Staff();
            staff.setFirstName(staffDto.getFirstName());
            staff.setLastName(staffDto.getLastName());
            staff.setPassword(passwordEncoder.encode(staffDto.getPassword()));
            staff.setEmail(staffDto.getEmail());
            staff.setPhoneNumber(staffDto.getPhoneNumber());
            staff.setDepartment(staffDto.getDepartment());
            staff.setCity(staffDto.getCity());
            staff.setDesignation(staffDto.getDesignation());
            staffService.saveNewUser(staff);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody StaffDto staffDto){
        try{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(staffDto.getEmail(),staffDto.getPassword()));
        String token  = jwtUtil.generateToken(staffDto.getEmail(),"STAFF");
        return new ResponseEntity<>(token,HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid Credentials",HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
