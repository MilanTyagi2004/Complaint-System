package com.SIH.SIH.services;

import com.SIH.SIH.dto.ComplaintDto;
import com.SIH.SIH.entity.Complaint;
import com.SIH.SIH.entity.ComplaintStatus;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.ComplaintRepository;
import com.SIH.SIH.repostitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;


    public Complaint autoSignoff(Complaint complaint){
        User staff = userRepository.findByDepartmentAndCity(complaint.getDepartment(), complaint.getCity())
                .stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("No staff available for this department in this city"));

        complaint.setStatus(ComplaintStatus.ASSIGNED);
        complaint.setStaffId(staff.getId());
        return complaint;
    }


    public Complaint createComplaint(ComplaintDto complaintDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        if ("anonymousUser".equals(email)) {
            throw new RuntimeException("User is not authenticated");
        }

        User userInDb = userRepository.findByEmail(email);
        if (userInDb == null) {
            throw new RuntimeException("User not found");
        }

        if (!userInDb.isVerified()) {
            throw new RuntimeException("Verify phone number");
        }

        Complaint complaint = new Complaint();
        complaint.setTitle(complaintDto.getTitle());
        complaint.setDescription(complaintDto.getDescription());
        complaint.setPincode(complaintDto.getPincode());
        complaint.setCity(complaintDto.getCity());
        complaint.setState(complaintDto.getState());
        complaint.setDepartment(complaintDto.getDepartment());
        complaint.setUserId(userInDb.getId());

        // Custom business logic
        complaint = autoSignoff(complaint);

        // Save complaint
        complaintRepository.save(complaint);

        // Send email
        emailService.sendEmail(email, "New complaint",
                "Created regarding the " + complaintDto.getDepartment() + " issue");

        return complaint;
    }

    public List<Complaint> getAllComplaint(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if(email.equals("anonymousUser")){
            throw new RuntimeException("user not authenticate");
        }
        User userInDb = userRepository.findByEmail(email);
        if(userInDb==null){
            throw new RuntimeException("User not found");        }

        return complaintRepository.findByStaffId(userInDb.getId());
    }

    public Complaint getComplaint(String complaintId) {
        return complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
    }

    public void deleteComplaint(String complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaintRepository.delete(complaint);
    }

    public Complaint updateComplaintStatus(String ComplaintId,ComplaintStatus status){
            Complaint complaint = complaintRepository.findById(ComplaintId).stream().findAny().orElseThrow(()-> new RuntimeException("complaint not found"));
            complaint.setStatus(status);
            complaint = complaintRepository.save(complaint);
            String userInDb = complaint.getUserId();
            User user = userRepository.findById(userInDb).stream().findAny().orElseThrow(()-> new RuntimeException("not found"));
            emailService.sendEmail(user.getEmail(),"Complain Status","complain status is changed to "+complaint.getStatus());
            return complaint;
    }
}
