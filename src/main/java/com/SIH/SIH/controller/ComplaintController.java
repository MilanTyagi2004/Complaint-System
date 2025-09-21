package com.SIH.SIH.controller;

import com.SIH.SIH.dto.ComplaintDto;
import com.SIH.SIH.entity.Complaint;
import com.SIH.SIH.exception.ResourceNotFoundException;
import com.SIH.SIH.services.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name ="Complaint APIs",description = "Complaint management endpoints for users")
public class ComplaintController {
    
    @Autowired
    private ComplaintService complaintService;

    @PostMapping("/newComplaint")
    @Operation(summary = "Create a new complaint", description = "Submit a new complaint with validation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Complaint created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<Complaint> createComplaint(@Valid @RequestBody ComplaintDto complaintDto) {
        Complaint createdComplaint = complaintService.createComplaint(complaintDto);
        return new ResponseEntity<>(createdComplaint, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get all complaints", description = "Retrieve all complaints for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Complaints retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        List<Complaint> complaints = complaintService.getAllComplaint();
        return new ResponseEntity<>(complaints, HttpStatus.OK);
    }

    @GetMapping("/id/{complaintId}")
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

    @DeleteMapping("/id/{complaintId}")
    @Operation(summary = "Delete complaint", description = "Delete a complaint by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Complaint deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Complaint not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<Void> deleteComplaint(@PathVariable String complaintId) {
        complaintService.deleteComplaint(complaintId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
