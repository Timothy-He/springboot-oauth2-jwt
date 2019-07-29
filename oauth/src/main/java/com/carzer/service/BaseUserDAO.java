package com.carzer.service;

import com.carzer.model.BaseUserDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BaseUserDAO {


    public BaseUserDTO findByLoginName(String loginName) {
        if (loginName.equals("user")) {
            BaseUserDTO baseUser = new BaseUserDTO();
            baseUser.setLoginName("user");
            baseUser.setPassword(new BCryptPasswordEncoder().encode("user"));
            baseUser.setRoles(new String[]{"ROLE_USER"});
            return baseUser;
        }
        if (loginName.equals("admin")) {
            BaseUserDTO adminUser = new BaseUserDTO();
            adminUser.setLoginName("admin");
            adminUser.setPassword(new BCryptPasswordEncoder().encode("admin"));
            adminUser.setRoles(new String[]{"ROLE_USER", "ROLE_ADMIN"});
            return adminUser;
        }
        return null;
    }
}
