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

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "User")
public class User {
    @Id
    private String id;
    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    @Indexed(unique = true)
    private String mobileNumber;

    @NonNull
    @Indexed(unique = true)
    private String email;

    @NonNull
    private String password;

    private boolean verified = false;
    private boolean active = true;

    private LocalDate dateOfBirth;
    private String gender;
    private String address;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;


}
