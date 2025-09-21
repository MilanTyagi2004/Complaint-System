package com.SIH.SIH.controller;

import com.SIH.SIH.entity.Complaint;
import com.SIH.SIH.entity.ComplaintStatus;
import com.SIH.SIH.repostitory.ComplaintRepository;
import com.SIH.SIH.services.ComplaintService;
import com.SIH.SIH.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("staff")
public class StaffController {

    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private ComplaintRepository complaintRepository;

    @GetMapping("/getAllComplaint")
    public ResponseEntity<?> getAllComplaint(){
        try {
            List<Complaint> complaints = complaintService.getAllComplaint();
            return  new ResponseEntity<>(complaints,HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/id/{complaintId}/status")
    public ResponseEntity<?> updateComplaint(@PathVariable String complaintId, @RequestParam ComplaintStatus status){
        try{
            Complaint complaint = complaintService.updateComplaintStatus(complaintId,status);
            return new ResponseEntity<>(complaint, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


}
