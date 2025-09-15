package com.SIH.SIH.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Staff")
public class Staff {
    @Id
    private String id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;
    @NonNull
    private String password;

    @NonNull
    @Indexed(unique = true)
    private String email;

    @NonNull
    private String phoneNumber;
    @NonNull
    private String department;

    private String designation;
    private String city;
    private String officeLocation;

    private boolean active = true;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
