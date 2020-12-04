package com.liu.controller;

import com.liu.entity.CheckUserEntity;
import com.liu.service.CheckUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private CheckUserService dateUserService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public String registerUser( CheckUserEntity registerUser){
        CheckUserEntity user = new CheckUserEntity();
        user.setUserName(registerUser.getUserName());
        user.setPassword(bCryptPasswordEncoder.encode(registerUser.getPassword()));
        user.setRole("ROLE_USER");
        dateUserService.save(user);
        return "success";
    }
}
