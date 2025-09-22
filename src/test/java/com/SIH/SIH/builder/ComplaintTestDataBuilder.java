package com.SIH.SIH.builder;

import com.SIH.SIH.dto.ComplaintDto;
import com.SIH.SIH.entity.Complaint;
import com.SIH.SIH.entity.ComplaintStatus;
import com.SIH.SIH.entity.Priority;

import java.time.LocalDateTime;

public class ComplaintTestDataBuilder {
    
    public static ComplaintDto createComplaintDto() {
        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setTitle("Water Supply Issue");
        complaintDto.setDescription("No water supply in our area for the past 3 days");
        complaintDto.setPincode(203209);
        complaintDto.setCity("noida");
        complaintDto.setState("up");
        complaintDto.setDepartment("water");
        return complaintDto;
    }
    
    public static Complaint createComplaint() {
        Complaint complaint = new Complaint();
        complaint.setTitle("Water Supply Issue");
        complaint.setDescription("No water supply in our area for the past 3 days");
        complaint.setPincode(203209);
        complaint.setCity("noida");
        complaint.setState("up");
        complaint.setDepartment("water");
        complaint.setPriority(Priority.MEDIUM);
        complaint.setStatus(ComplaintStatus.PENDING);
        complaint.setCreatedAt(LocalDateTime.now());
        complaint.setUpdatedAt(LocalDateTime.now());
        return complaint;
    }
    
    public static ComplaintDto createValidComplaintDto() {
        return createComplaintDto();
    }
    
    public static ComplaintDto createHighPriorityComplaintDto() {
        ComplaintDto complaintDto = createComplaintDto();
        complaintDto.setTitle("Emergency: Gas Leak");
        complaintDto.setDescription("Gas leak detected in building, immediate action required");
        complaintDto.setDepartment("water");
        return complaintDto;
    }
    
    public static Complaint createValidComplaint() {
        return createComplaint();
    }
    
    public static Complaint createAssignedComplaint() {
        Complaint complaint = createComplaint();
        complaint.setStatus(ComplaintStatus.ASSIGNED);
        complaint.setStaffId("staff123");
        complaint.setUserId("user123");
        return complaint;
    }
    
    public static Complaint createResolvedComplaint() {
        Complaint complaint = createComplaint();
        complaint.setStatus(ComplaintStatus.RESOLVED);
        complaint.setStaffId("staff123");
        complaint.setUserId("user123");
        return complaint;
    }
}
