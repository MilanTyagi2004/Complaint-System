package com.SIH.SIH.controller;

import com.SIH.SIH.dto.ComplaintDto;
import com.SIH.SIH.entity.Complaint;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.ComplaintRepository;
import com.SIH.SIH.repostitory.StaffRepository;
import com.SIH.SIH.repostitory.UserRepository;
import com.SIH.SIH.services.ComplaintService;
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
@RequestMapping("/user/complaint")
public class UserComplaintController {
    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private EmailService emailService;

    @PostMapping("/newComplaint")
    public ResponseEntity<?> newComplain(@RequestBody ComplaintDto complaintDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if(email.equals("anonymousUser")){
            return new ResponseEntity<>("User is not authenticated",HttpStatus.UNAUTHORIZED);
        }
        User userInDb = userRepository.findByEmail(email);
        if(userInDb==null){
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        }
        if(!userInDb.isVerified()){
            return new ResponseEntity<>("verify phone number ",HttpStatus.NOT_ACCEPTABLE);
        }
        String userId = userInDb.getId();
        try{
            Complaint complaint = new Complaint();
            complaint.setTitle(complaintDto.getTitle());
            complaint.setDescription(complaintDto.getDescription());
            complaint.setPincode(complaintDto.getPincode());
            complaint.setCity(complaintDto.getCity());
            complaint.setState(complaintDto.getState());
            complaint.setDepartment(complaintDto.getDepartment());
            complaint.setUserId(userId);
            complaint = complaintService.autoSignoff(complaint);
            complaintService.saveComplaint(complaint);
            emailService.sendEmail(email,"new complaint","created regarding to the "+ complaintDto.getDepartment()+ " issue");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(complaintDto,HttpStatus.CREATED);
    }
    @GetMapping()
    public ResponseEntity<?>getAllComplaint(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User userInDb = userRepository.findByEmail(email);
        if(userInDb==null){
            return new ResponseEntity<>("user not found",HttpStatus.NOT_FOUND);
        }
        String userId =userInDb.getId();
        List<Complaint> complaints = complaintRepository.findByUserId(userId);
        return new ResponseEntity<>(complaints,HttpStatus.OK);
    }
    @GetMapping("/id/{myID}")
    public ResponseEntity<?> getComplain(@PathVariable String myID){
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User userInDb = userRepository.findByEmail(email);
        if(userInDb==null){
            return new ResponseEntity<>("user not found",HttpStatus.NOT_FOUND);
        }
        String userId =userInDb.getId();
        List<Complaint> complaints = complaintService.getComplaint(userId);
        return new ResponseEntity<>(complaints, HttpStatus.OK);
    }
    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteComplain(@PathVariable String myId){
        Optional<Complaint> complaint = complaintRepository.findById(myId);
        if(complaint.isPresent()){
            complaintRepository.deleteById(myId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
