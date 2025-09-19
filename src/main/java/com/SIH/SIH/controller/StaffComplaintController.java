package com.SIH.SIH.controller;

import com.SIH.SIH.entity.Complaint;
import com.SIH.SIH.entity.ComplaintStatus;
import com.SIH.SIH.entity.Staff;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.ComplaintRepository;
import com.SIH.SIH.repostitory.StaffRepository;
import com.SIH.SIH.repostitory.UserRepository;
import com.SIH.SIH.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/staff/complaint")
public class StaffComplaintController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private EmailService emailService;
    @GetMapping()
    public ResponseEntity<?> getComplaint(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Staff staffInDb = staffRepository.findByEmail(email);
            if(staffInDb==null){
                return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
            }
            String staffId = staffInDb.getId();
            List<Complaint> complaints = complaintRepository.findByStaffId(staffId);
            return new ResponseEntity<>(complaints,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateComplaint(@PathVariable String id,@RequestParam ComplaintStatus status){
        try{
            Complaint complaint = complaintRepository.findById(id).stream().findAny().orElseThrow(()-> new RuntimeException("complaint not found"));
            complaint.setStatus(status);
            complaint = complaintRepository.save(complaint);
            String userInDb = complaint.getUserId();
            User user = userRepository.findById(userInDb).stream().findAny().orElseThrow(()-> new RuntimeException("not found"));
            emailService.sendEmail(user.getEmail(),"Complain Status","complain status is changed and new status is "+complaint.getStatus());
            return new ResponseEntity<>(complaint,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
