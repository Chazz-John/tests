package com.zhao.shirospringboot.service;

import com.zhao.shirospringboot.entity.UserInfo;

/**
 * 描述:
 *
 * @author Chazz
 * @create 2020-11-10 9:44
 */
public interface UserInfoService {
    UserInfo findUserInfoByName(String name);
    int addUserInfo(UserInfo userInfo);
}
