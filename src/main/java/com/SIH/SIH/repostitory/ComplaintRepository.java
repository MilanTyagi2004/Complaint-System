package com.SIH.SIH.repostitory;

import com.SIH.SIH.entity.Complaint;
import com.SIH.SIH.entity.ComplaintStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ComplaintRepository extends MongoRepository<Complaint,String> {
    List<Complaint> findByUserId(String userId);
    List<Complaint> findByStaffId(String staffId);
    void deleteById(String id);
}
