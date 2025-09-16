package com.SIH.SIH.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {
    private String firstName;

    private String lastName;

    private String password;

    private String email;

    private String phoneNumber;
    private String department;
    private String city;


    private String designation;

}
