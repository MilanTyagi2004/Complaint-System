package com.SIH.SIH.repostitory;

import com.SIH.SIH.entity.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends MongoRepository<Staff,String> {
    Staff findByEmail(String email);
    void deleteByEmail(String email);
     List<Staff> findByDepartmentAndCity(String department, String city);
}
