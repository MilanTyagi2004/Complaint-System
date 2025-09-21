package com.SIH.SIH.controller;

import com.SIH.SIH.dto.ComplaintDto;
import com.SIH.SIH.entity.Complaint;
import com.SIH.SIH.repostitory.ComplaintRepository;
import com.SIH.SIH.repostitory.UserRepository;
import com.SIH.SIH.services.ComplaintService;
import com.SIH.SIH.services.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name ="Complaint APIs",description = "new complaint,get complaint,delete complain,get complaint by ID")
public class ComplaintController {
    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private EmailService emailService;

    @PostMapping("/newComplaint")
    public ResponseEntity<?> newComplain(@RequestBody ComplaintDto complaintDto) {
        try {
            Complaint createdComplaint = complaintService.createComplaint(complaintDto);
            return new ResponseEntity<>(createdComplaint, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?>getAllComplaint(){
        try {
            List<Complaint> complaints = complaintService.getAllComplaint();
            return new ResponseEntity<>(complaints, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{complaintId}")
    public ResponseEntity<?> getComplain(@PathVariable String complaintId){
        Complaint complaint = complaintService.getComplaint(complaintId);
        return new ResponseEntity<>(complaint, HttpStatus.OK);
    }

    @DeleteMapping("/id/{complaintId}")
    public ResponseEntity<?> deleteComplain(@PathVariable String complaintId){
        try {
            complaintService.deleteComplaint(complaintId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
