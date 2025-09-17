package com.SIH.SIH.services;

import com.SIH.SIH.entity.Staff;
import com.SIH.SIH.repostitory.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void saveUser(Staff staff) {
        staffRepository.save(staff);
    }

    public void saveUpdatePassword(Staff staffInDb) {
        staffInDb.setPassword(passwordEncoder.encode(staffInDb.getPassword()));
        staffRepository.save(staffInDb);
    }
}
