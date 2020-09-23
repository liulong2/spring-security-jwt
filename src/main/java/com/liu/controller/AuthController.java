package com.liu.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liu.entity.CheckUserEntity;
import com.liu.mapper.CheckUserDAO;
import com.liu.service.CheckUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
//@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private CheckUserService dateUserService;

    @Autowired
    private CheckUserDAO checkUserDAO;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/register")
    public String registerUser(@RequestBody Map<String,String> registerUser){
        CheckUserEntity user = new CheckUserEntity();
        user.setUserName(registerUser.get("username"));
        user.setPassword(bCryptPasswordEncoder.encode(registerUser.get("password")));
        user.setRole("ROLE_USER");
        dateUserService.save(user);
        return "success";
    }
    @GetMapping("/list")
    public String getdataUset() {
        List<CheckUserEntity> checkUserEntities = checkUserDAO.selectList(new LambdaQueryWrapper<CheckUserEntity>());
        return JSON.toJSON(checkUserEntities).toString();
    }
}
