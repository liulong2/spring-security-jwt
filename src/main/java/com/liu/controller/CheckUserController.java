package com.liu.controller;


import com.liu.service.CheckUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liu
 * @since 2020-05-27
 */
@RestController
@RequestMapping("/check/user/entity")
public class CheckUserController {
    @Autowired
    private CheckUserService checkuserentityService;

}
