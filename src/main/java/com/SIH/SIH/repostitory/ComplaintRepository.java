package com.SIH.SIH.repostitory;

import com.SIH.SIH.entity.Complaint;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ComplaintRepository extends MongoRepository<Complaint,String> {
}
