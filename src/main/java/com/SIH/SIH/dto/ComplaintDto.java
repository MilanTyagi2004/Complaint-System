package com.SIH.SIH.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDto {
    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;
    
    @NotNull(message = "Pincode is required")
    @Min(value = 100000, message = "Pincode must be a valid 6-digit number")
    @Max(value = 999999, message = "Pincode must be a valid 6-digit number")
    private Integer pincode;
    
    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50, message = "City name must be between 2 and 50 characters")
    private String city;
    
    @NotBlank(message = "State is required")
    @Size(min = 2, max = 50, message = "State name must be between 2 and 50 characters")
    private String state;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;
    
    @NotBlank(message = "Department is required")
    @Size(min = 2, max = 50, message = "Department name must be between 2 and 50 characters")
    private String department;
}
