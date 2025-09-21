package com.SIH.SIH.repostitory;

import com.SIH.SIH.entity.Complaint;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ComplaintRepository extends MongoRepository<Complaint,String> {
    List<Complaint> findByUserId(String userId);
    void deleteById(@NotNull String id);

    List<Complaint> findByStaffId(String StaffId);
}
