package com.SIH.SIH.dto;

import com.SIH.SIH.entity.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDto {
    private String title;
    private Integer pincode;
    private String city;
    private String state;
    private String description;
    private String department;
}
