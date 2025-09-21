package com.SIH.SIH.dto;

import com.SIH.SIH.entity.Priority;
import com.SIH.SIH.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    private String phoneNumber;

    private Role role;
    private String department;
    private String designation;
    private String city;// only staff


    private boolean verified = false;
    private boolean active = true;
}
