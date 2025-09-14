package com.SIH.SIH.repostitory;

import com.SIH.SIH.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
}
