package com.SIH.SIH.repostitory;

import com.SIH.SIH.entity.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StaffRepository extends MongoRepository<Staff,String> {
    Staff findByEmail(String email);
    void deleteByEmail(String email);
}
