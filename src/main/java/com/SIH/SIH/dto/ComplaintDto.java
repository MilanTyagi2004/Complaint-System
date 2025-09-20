package com.SIH.SIH.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDto {
    @NonNull
    private String title;
    @NonNull
    private Integer pincode;
    @NonNull
    private String city;
    @NonNull
    private String state;
    @NonNull
    private String description;
    @NonNull
    private String department;
}
