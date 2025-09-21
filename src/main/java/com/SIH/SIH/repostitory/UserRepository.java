package com.SIH.SIH.repostitory;

import com.SIH.SIH.entity.Role;
import com.SIH.SIH.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User,String> {
    User findByEmail(String email);
    List<User> findByDepartmentAndCity(String department, String city);
    void deleteByEmail(String email);
    List<User> findByRole(Role role);
}
