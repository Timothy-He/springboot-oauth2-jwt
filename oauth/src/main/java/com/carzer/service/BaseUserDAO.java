package com.carzer.service;

import com.carzer.model.BaseUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BaseUserDAO {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BaseUserDAO(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public BaseUserDTO findByLoginName(String loginName) {
        if (loginName.equals("user")) {
            BaseUserDTO baseUser = new BaseUserDTO();
            baseUser.setLoginName("user");
            baseUser.setPassword(passwordEncoder.encode("user"));
            baseUser.setRoles(new String[]{"ROLE_USER"});
            return baseUser;
        }
        if (loginName.equals("admin")) {
            BaseUserDTO adminUser = new BaseUserDTO();
            adminUser.setLoginName("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setRoles(new String[]{"ROLE_USER", "ROLE_ADMIN"});
            return adminUser;
        }
        return null;
    }
}
