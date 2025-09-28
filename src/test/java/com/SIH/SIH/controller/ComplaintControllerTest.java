package com.SIH.SIH.controller;

import com.SIH.SIH.builder.ComplaintTestDataBuilder;
import com.SIH.SIH.dto.ComplaintDto;
import com.SIH.SIH.entity.Complaint;
import com.SIH.SIH.services.ComplaintService;
import com.SIH.SIH.services.UserDetailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ComplaintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComplaintService complaintService;

    @MockitoBean
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    private ComplaintDto validComplaintDto;
    private Complaint validComplaint;

    @BeforeEach
    void setUp() {
        validComplaintDto = ComplaintTestDataBuilder.createValidComplaintDto();
        validComplaint = ComplaintTestDataBuilder.createValidComplaint();
    }

    @Test
    @WithMockUser
    void createComplaint_WithValidData_ShouldReturnCreated() throws Exception {
        // Given
        when(complaintService.createComplaint(any(ComplaintDto.class))).thenReturn(validComplaint);

        // When & Then
        mockMvc.perform(post("/user/newComplaint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validComplaintDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(validComplaintDto.getTitle()))
                .andExpect(jsonPath("$.description").value(validComplaintDto.getDescription()));
    }

    @Test
    @WithMockUser
    void createComplaint_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        ComplaintDto invalidComplaintDto = ComplaintTestDataBuilder.createComplaintDto();
        invalidComplaintDto.setTitle(""); // Invalid: empty title
        invalidComplaintDto.setDescription("Short"); // Invalid: too short description

        // When & Then
        mockMvc.perform(post("/user/newComplaint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidComplaintDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    @WithMockUser
    void getAllComplaints_ShouldReturnComplaintsList() throws Exception {
        // Given
        List<Complaint> complaints = Arrays.asList(
                ComplaintTestDataBuilder.createValidComplaint(),
                ComplaintTestDataBuilder.createAssignedComplaint()
        );
        when(complaintService.getAllComplaint()).thenReturn(complaints);

        // When & Then
        mockMvc.perform(get("/user/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser
    void getComplaint_WithValidId_ShouldReturnComplaint() throws Exception {
        // Given
        String complaintId = "complaint123";
        when(complaintService.getComplaint(complaintId)).thenReturn(validComplaint);

        // When & Then
        mockMvc.perform(get("/user/id/{complaintId}", complaintId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(validComplaint.getTitle()));
    }

    @Test
    @WithMockUser
    void getComplaint_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Given
        String complaintId = "nonexistent";
        when(complaintService.getComplaint(complaintId)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/user/id/{complaintId}", complaintId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource Not Found"));
    }

    @Test
    @WithMockUser
    void deleteComplaint_WithValidId_ShouldReturnNoContent() throws Exception {
        // Given
        String complaintId = "complaint123";

        // When & Then
        mockMvc.perform(delete("/user/id/{complaintId}", complaintId))
                .andExpect(status().isNoContent());
    }

    @Test
    void createComplaint_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        // When & Then
        mockMvc.perform(post("/user/newComplaint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validComplaintDto)))
                .andExpect(status().isUnauthorized());
    }
}
