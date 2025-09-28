package com.SIH.SIH.integration;

import com.SIH.SIH.builder.ComplaintTestDataBuilder;
import com.SIH.SIH.builder.UserTestDataBuilder;
import com.SIH.SIH.dto.ComplaintDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.SIH.SIH.entity.Complaint;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.ComplaintRepository;
import com.SIH.SIH.repostitory.UserRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ComplaintIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private User testStaff;
    private ComplaintDto validComplaintDto;

    @BeforeEach
    void setUp() {
        // Clean up test data
        complaintRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        testUser = UserTestDataBuilder.createValidUser();
        testUser.setVerified(true);
        testUser = userRepository.save(testUser);

        // Create test staff
        testStaff = UserTestDataBuilder.createStaffUser();
        testStaff = userRepository.save(testStaff);

        // Create valid complaint DTO
        validComplaintDto = ComplaintTestDataBuilder.createValidComplaintDto();
    }

    @Test
    @WithMockUser(username = "john.doe@example.com")
    void createComplaint_IntegrationTest() throws Exception {
        // When & Then
        mockMvc.perform(post("/user/newComplaint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validComplaintDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(validComplaintDto.getTitle()))
                .andExpect(jsonPath("$.description").value(validComplaintDto.getDescription()))
                .andExpect(jsonPath("$.status").value("ASSIGNED"));

        // Verify complaint was saved in database
        assertEquals(1, complaintRepository.count());
    }

    @Test
    @WithMockUser(username = "staff@example.com")
    void getAllComplaints_IntegrationTest() throws Exception {
        // Given - Create a complaint first
        Complaint complaint = ComplaintTestDataBuilder.createAssignedComplaint();
        complaint.setStaffId(testStaff.getId());
        complaintRepository.save(complaint);

        // When & Then
        mockMvc.perform(get("/user/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "john.doe@example.com")
    void getComplaintById_IntegrationTest() throws Exception {
        // Given - Create a complaint first
        Complaint complaint = ComplaintTestDataBuilder.createValidComplaint();
        complaint.setUserId(testUser.getId());
        complaint = complaintRepository.save(complaint);

        // When & Then
        mockMvc.perform(get("/user/id/{complaintId}", complaint.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(complaint.getTitle()))
                .andExpect(jsonPath("$.description").value(complaint.getDescription()));
    }

    @Test
    @WithMockUser(username = "john.doe@example.com")
    void deleteComplaint_IntegrationTest() throws Exception {
        // Given - Create a complaint first
        Complaint complaint = ComplaintTestDataBuilder.createValidComplaint();
        complaint.setUserId(testUser.getId());
        complaint = complaintRepository.save(complaint);

        // When & Then
        mockMvc.perform(delete("/user/id/{complaintId}", complaint.getId()))
                .andExpect(status().isNoContent());

        // Verify complaint was deleted from database
        assertEquals(0, complaintRepository.count());
    }

    @Test
    void createComplaint_WithoutAuthentication_ShouldReturnUnauthorizedOrForbidden() throws Exception {
        // When & Then
        mockMvc.perform(post("/user/newComplaint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validComplaintDto)))
                .andExpect(status().is4xxClientError()) // Accepts both 401 and 403
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 401 || status == 403, 
                        "Expected 401 or 403, but got: " + status);
                });
    }

    @Test
    @WithMockUser(username = "john.doe@example.com")
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
}
