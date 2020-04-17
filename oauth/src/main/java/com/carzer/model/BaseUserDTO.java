package com.carzer.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by WangHQ on 2017/7/6 0006.
 */
@Data
public class BaseUserDTO implements Serializable {

    private static final long serialVersionUID = 7815723327741280024L;
    private String id;
    private String loginName;
    private String userName;
    private String nickName;
    private String password;
    private String[] roles;
    private String depart;

}
