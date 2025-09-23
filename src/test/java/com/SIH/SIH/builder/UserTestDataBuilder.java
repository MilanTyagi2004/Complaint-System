package com.SIH.SIH.builder;

import com.SIH.SIH.dto.UserDto;
import com.SIH.SIH.entity.Role;
import com.SIH.SIH.entity.User;

public class UserTestDataBuilder {
    
    public static UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("Test@123");
        userDto.setPhoneNumber("9876543210");
        userDto.setRole(Role.USER);
        userDto.setCity("noida");
        userDto.setVerified(false);
        userDto.setActive(true);
        return userDto;
    }
    
    public static User createUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("Test@123");
        user.setPhoneNumber("9876543210");
        user.setRole(Role.USER);
        user.setCity("noida");
        user.setVerified(false);
        user.setActive(true);
        return user;
    }
    
    public static UserDto createValidUserDto() {
        return createUserDto();
    }
    
    public static UserDto createStaffDto() {
        UserDto userDto = createUserDto();
        userDto.setEmail("staff@example.com");
        userDto.setRole(Role.STAFF);
        userDto.setDepartment("water");
        userDto.setDesignation("Engineer");
        return userDto;
    }
    
    public static UserDto createAdminDto() {
        UserDto userDto = createUserDto();
        userDto.setEmail("admin@example.com");
        userDto.setRole(Role.ADMIN);
        return userDto;
    }
    
    public static User createValidUser() {
        return createUser();
    }
    
    public static User createStaffUser() {
        User user = createUser();
        user.setEmail("staff@example.com");
        user.setRole(Role.STAFF);
        user.setDepartment("water");
        user.setDesignation("Engineer");
        return user;
    }
}
