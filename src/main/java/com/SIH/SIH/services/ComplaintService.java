package com.SIH.SIH.services;

import com.SIH.SIH.entity.Complaint;
import com.SIH.SIH.entity.ComplaintStatus;
import com.SIH.SIH.entity.Staff;
import com.SIH.SIH.repostitory.ComplaintRepository;
import com.SIH.SIH.repostitory.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private StaffRepository staffRepository;
    public Complaint autoSignoff(Complaint complaint){
        try{
            Staff staff = staffRepository.findByDepartmentAndCity(complaint.getDepartment(), complaint.getCity()).stream().findAny().orElseThrow(()-> new RuntimeException("no available user"));
            complaint.setStatus(ComplaintStatus.ASSIGNED);
            complaint.setStaffId(staff.getId());
            return complaint;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveComplaint(Complaint complaint) {
        complaintRepository.save(complaint);
    }

    public List<Complaint> getComplaint(String userId) {
       return  complaintRepository.findByUserId(userId);
    }
}
