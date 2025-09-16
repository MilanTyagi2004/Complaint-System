package com.SIH.SIH.services;

import com.SIH.SIH.entity.Staff;
import com.SIH.SIH.repostitory.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    public void saveNewUser(Staff staff) {
        staffRepository.save(staff);
    }
}
