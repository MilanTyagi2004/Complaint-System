package com.SIH.SIH.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Complaint")
public class Complaint {
    @Id
    private String id;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private String department;

    @NonNull
    private Priority priority = Priority.MEDIUM;

    private ComplaintStatus status = ComplaintStatus.PENDING; // PENDING, IN_PROGRESS, RESOLVED, REJECTED

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String userId;   // who raised it
    private String staffId;  // auto-assigned staff

}
