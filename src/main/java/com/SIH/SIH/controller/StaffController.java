package com.SIH.SIH.controller;

import com.SIH.SIH.entity.Complaint;
import com.SIH.SIH.entity.ComplaintStatus;
import com.SIH.SIH.exception.ResourceNotFoundException;
import com.SIH.SIH.exception.ValidationException;
import com.SIH.SIH.services.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@Tag(name ="Staff APIs",description = "Staff complaint management endpoints")
public class StaffController {

    @Autowired
    private ComplaintService complaintService;

    @GetMapping("/complaints")
    @Operation(summary = "Get all complaints", description = "Retrieve all complaints for staff management")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Complaints retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        List<Complaint> complaints = complaintService.getAllComplaint();
        return new ResponseEntity<>(complaints, HttpStatus.OK);
    }

    @PutMapping("/complaints/{complaintId}/status")
    @Operation(summary = "Update complaint status", description = "Update the status of a specific complaint")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Complaint status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status or complaint ID"),
        @ApiResponse(responseCode = "404", description = "Complaint not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<Complaint> updateComplaintStatus(
            @PathVariable String complaintId, 
            @RequestParam ComplaintStatus status) {
        
        if (status == null) {
            throw new ValidationException("Status parameter is required");
        }
        
        Complaint complaint = complaintService.updateComplaintStatus(complaintId, status);
        if (complaint == null) {
            throw new ResourceNotFoundException("Complaint", "id", complaintId);
        }
        
        return new ResponseEntity<>(complaint, HttpStatus.OK);
    }

    @GetMapping("/complaints/{complaintId}")
    @Operation(summary = "Get complaint by ID", description = "Retrieve a specific complaint by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Complaint found"),
        @ApiResponse(responseCode = "404", description = "Complaint not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<Complaint> getComplaint(@PathVariable String complaintId) {
        Complaint complaint = complaintService.getComplaint(complaintId);
        if (complaint == null) {
            throw new ResourceNotFoundException("Complaint", "id", complaintId);
        }
        return new ResponseEntity<>(complaint, HttpStatus.OK);
    }
}
