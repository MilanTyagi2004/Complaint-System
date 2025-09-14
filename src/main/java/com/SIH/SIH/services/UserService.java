package com.SIH.SIH.services;

import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        if(userRepository.findByEmail(user.getEmail())!=null){
            throw new RuntimeException("email is already exists");
        }
        return userRepository.save(user);
    }

}
