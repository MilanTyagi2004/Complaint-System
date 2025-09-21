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
@Document(collection = "User")
public class User {
        @Id
        private String id;

        @NonNull
        private String firstName;
        @NonNull
        private String lastName;
        @NonNull
        private String email;
        @NonNull
        private String password;

        private String city;
        private String phoneNumber; // for user/staff
        private String department; // only staff
        private String designation; // only staff
        private boolean active = true;
        private boolean verified = false; // only user

        private Role role; // ENUM: USER, STAFF, ADMIN

        @CreatedDate
        private LocalDateTime createdAt;
        @LastModifiedDate
        private LocalDateTime updatedAt;
    }
