package com.liu.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liu.entity.CheckUserEntity;
import com.liu.entity.CheckRoleEntity;
import com.liu.mapper.CheckRoleDAO;
import com.liu.mapper.CheckUserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    //修改UserDetailsServiceImpl.java ，添加属性和setter方法 ，修改loadUserByUsername方法
    @Autowired
    private CheckUserDAO dateUserDAO;
    public void setDateUserDAO(CheckUserDAO dateUserDAO){
        this.dateUserDAO=dateUserDAO;
    }

    @Autowired
    private CheckRoleDAO roleDAO;
    public void setRoleDAO(CheckRoleDAO roleDAO){
        this.roleDAO=roleDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CheckUserEntity checkUserEntity = dateUserDAO.selectOne(new LambdaQueryWrapper<CheckUserEntity>()
                .eq(CheckUserEntity::getUserName,username));

        CheckRoleEntity checkRoleEntity = roleDAO.selectById(checkUserEntity.getRole());
        // 权限集合
        List<GrantedAuthority> authList = new ArrayList<>();

        // 具体具有什么的权限
        authList.add(new SimpleGrantedAuthority("ROLE_" + checkRoleEntity.getName()));
        //1 判断用户名是否为null 如果为null 直接返回null
        if(username==null){
            return null;
        }

        //3 如果用户查不到 返回null
        if(checkUserEntity !=null){
        //4 如果用户对象查到了 判断用户审核 是否通过 如果未通过返回null
            //if("1".equals(seller.getStatus())){
        //5 返回user 对象 将用户名 密码 返回权限集合

        //String encode = new BCryptPasswordEncoder().encode(dateUserEntity.getPassword());
        return new User(checkUserEntity.getUserName(), checkUserEntity.getPassword(),authList);
            //}

        }
        return null;
//6 框架帮助比对用户名和密码是否匹配
    }
}
