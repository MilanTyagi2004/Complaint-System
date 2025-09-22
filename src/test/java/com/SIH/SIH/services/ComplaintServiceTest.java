package com.SIH.SIH.services;

import com.SIH.SIH.builder.ComplaintTestDataBuilder;
import com.SIH.SIH.builder.UserTestDataBuilder;
import com.SIH.SIH.dto.ComplaintDto;
import com.SIH.SIH.entity.Complaint;
import com.SIH.SIH.entity.ComplaintStatus;
import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.ComplaintRepository;
import com.SIH.SIH.repostitory.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ComplaintService complaintService;

    private ComplaintDto validComplaintDto;
    private User validUser;
    private User staffUser;
    private Complaint validComplaint;

    @BeforeEach
    void setUp() {
        validComplaintDto = ComplaintTestDataBuilder.createValidComplaintDto();
        validUser = UserTestDataBuilder.createValidUser();
        staffUser = UserTestDataBuilder.createStaffUser();
        validUser.setVerified(true);
        validComplaint = ComplaintTestDataBuilder.createValidComplaint();

        // Mock security context
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createComplaint_WithValidData_ShouldCreateComplaint() {
        // Given
        when(authentication.getName()).thenReturn("milantyagi176@gmail.com");
        when(userRepository.findByEmail("milantyagi176@gmail.com")).thenReturn(validUser);
        when(userRepository.findByDepartmentAndCity(anyString(), anyString()))
                .thenReturn(Arrays.asList(staffUser));
        validUser.setVerified(true);
        when(complaintRepository.save(any(Complaint.class))).thenReturn(validComplaint);

        // When
        Complaint result = complaintService.createComplaint(validComplaintDto);

        // Then
        assertNotNull(result);
        assertEquals(validComplaintDto.getTitle(), result.getTitle());
        assertEquals(validComplaintDto.getDescription(), result.getDescription());
        verify(complaintRepository).save(any(Complaint.class));
        verify(emailService).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void createComplaint_WithUnauthenticatedUser_ShouldThrowException() {
        // Given
        when(authentication.getName()).thenReturn("anonymousUser");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> complaintService.createComplaint(validComplaintDto));
        assertEquals("User is not authenticated", exception.getMessage());
    }

    @Test
    void createComplaint_WithUnverifiedUser_ShouldThrowException() {
        // Given
        when(authentication.getName()).thenReturn("john.doe@example.com");
        User unverifiedUser = UserTestDataBuilder.createUser();
        unverifiedUser.setVerified(false);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(unverifiedUser);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> complaintService.createComplaint(validComplaintDto));
        assertEquals("Verify phone number", exception.getMessage());
    }

    @Test
    void createComplaint_WithNoAvailableStaff_ShouldThrowException() {
        // Given
        when(authentication.getName()).thenReturn("milantyagi176@gmail.com");
        when(userRepository.findByEmail("milantyagi176@gmail.com")).thenReturn(validUser);
        validUser.setVerified(true);
        when(userRepository.findByDepartmentAndCity(anyString(), anyString()))
                .thenReturn(Arrays.asList()); // No staff available

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> complaintService.createComplaint(validComplaintDto));
        assertEquals("No staff available for this department in this city", exception.getMessage());
    }

    @Test
    void getAllComplaint_WithValidUser_ShouldReturnComplaints() {
        // Given
        when(authentication.getName()).thenReturn("staff@example.com");
        when(userRepository.findByEmail("staff@example.com")).thenReturn(staffUser);
        List<Complaint> expectedComplaints = Arrays.asList(
                ComplaintTestDataBuilder.createValidComplaint(),
                ComplaintTestDataBuilder.createAssignedComplaint()
        );
        when(complaintRepository.findByStaffId(staffUser.getId())).thenReturn(expectedComplaints);

        // When
        List<Complaint> result = complaintService.getAllComplaint();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(complaintRepository).findByStaffId(staffUser.getId());
    }

    @Test
    void getComplaint_WithValidId_ShouldReturnComplaint() {
        // Given
        String complaintId = "68cf8915c0e73c42c0e74a56";
        when(complaintRepository.findById(complaintId)).thenReturn(Optional.of(validComplaint));

        // When
        Complaint result = complaintService.getComplaint(complaintId);

        // Then
        assertNotNull(result);
        assertEquals(validComplaint.getTitle(), result.getTitle());
    }

    @Test
    void getComplaint_WithInvalidId_ShouldThrowException() {
        // Given
        String complaintId = "nonexistent";
        when(complaintRepository.findById(complaintId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> complaintService.getComplaint(complaintId));
        assertEquals("Complaint not found", exception.getMessage());
    }

    @Test
    void updateComplaintStatus_WithValidData_ShouldUpdateStatus() {
        // Given
        String complaintId = "complaint123";
        ComplaintStatus newStatus = ComplaintStatus.IN_PROGRESS;
        Complaint complaint = ComplaintTestDataBuilder.createAssignedComplaint();
        complaint.setId(complaintId);
        
        when(complaintRepository.findById(complaintId)).thenReturn(Optional.of(complaint));
        when(complaintRepository.save(any(Complaint.class))).thenReturn(complaint);
        when(userRepository.findById(complaint.getUserId())).thenReturn(Optional.of(validUser));

        // When
        Complaint result = complaintService.updateComplaintStatus(complaintId, newStatus);

        // Then
        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());
        verify(emailService).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void deleteComplaint_WithValidId_ShouldDeleteComplaint() {
        // Given
        String complaintId = "68cf8915c0e73c42c0e74a56";
        when(complaintRepository.findById(complaintId)).thenReturn(Optional.of(validComplaint));

        // When
        complaintService.deleteComplaint(complaintId);

        // Then
        verify(complaintRepository).delete(validComplaint);
    }
}
