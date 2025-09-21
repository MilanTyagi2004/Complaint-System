package com.SIH.SIH.controller;

import com.SIH.SIH.entity.User;
import com.SIH.SIH.repostitory.UserRepository;
import com.SIH.SIH.services.SmsService;
import com.SIH.SIH.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("otp")
public class OtpController {
    @Autowired
    private UserRepository userRepository;
    private final SmsService smsService;
    private final Map<String, String> otpStorage = new HashMap<>(); // replace with DB/Redis

    public OtpController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send")
    public String sendOtp() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User userInDb = userRepository.findByEmail(email);
        String phoneNumber = userInDb.getPhoneNumber();
        String formattedNumber = "+91" + phoneNumber;

        String otp = OtpUtil.generateOtp();
        otpStorage.put(formattedNumber, otp);
        smsService.sendOtp(formattedNumber, otp);
        return "OTP sent to " + formattedNumber;
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String otp) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User userInDb = userRepository.findByEmail(email);
        String phoneNumber = userInDb.getPhoneNumber();
        String formattedNumber = "+91" + phoneNumber;

        String storedOtp = otpStorage.get(formattedNumber);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(formattedNumber);
            userInDb.setVerified(true);
            userRepository.save(userInDb);
            return "OTP verified successfully!";
        }
        return "Invalid OTP!";
    }
}
