package com.SIH.SIH.services;

import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        if(userRepository.findByEmail(user.getEmail())!=null){
            throw new RuntimeException("email is already exists");
        }
        return userRepository.save(user);
    }
    public void saveUserPassword(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}
