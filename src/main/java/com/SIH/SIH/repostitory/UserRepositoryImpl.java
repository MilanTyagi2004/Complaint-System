package com.SIH.SIH.repostitory;

import com.SIH.SIH.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUserforSA(){
        Query query =  new Query();

//        create a OR criteria query
        Criteria criteria = new Criteria();

        query.addCriteria(criteria.orOperator(
                Criteria.where("email").exists(true)));

//        above addCriteris satisfied in AND term means they both run
        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}
