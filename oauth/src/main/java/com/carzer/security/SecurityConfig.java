package com.carzer.security;

import com.carzer.model.BaseUserDTO;
import com.carzer.service.BaseUserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final BaseUserDAO baseUserDAO;

    @Autowired
    public SecurityConfig(BaseUserDAO baseUserDAO) {
        this.baseUserDAO = baseUserDAO;
    }

    @Autowired // <-- This is crucial otherwise Spring Boot creates its own
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .and().logout().permitAll()//退出页面
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .anonymous().disable()
                .authorizeRequests().anyRequest().authenticated()
        ;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return name -> {
            // 通过用户名获取用户信息
            BaseUserDTO userDTO = baseUserDAO.findByLoginName(name);
            if (userDTO != null) {
                // 创建spring security安全用户
                return new User(userDTO.getLoginName(), userDTO.getPassword(),
                        AuthorityUtils.createAuthorityList(userDTO.getRoles()));
            } else {
                throw new UsernameNotFoundException("用户[" + name + "]不存在");
            }
        };
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
