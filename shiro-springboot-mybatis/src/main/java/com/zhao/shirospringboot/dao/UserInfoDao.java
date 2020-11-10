package com.zhao.shirospringboot.dao;

import com.zhao.shirospringboot.entity.UserInfo;

/**
 * 描述:
 *
 * @author Chazz
 * @create 2020-11-10 9:40
 */
public interface UserInfoDao {
    UserInfo findUserInfoByName(String name);
    int addUserInfo(UserInfo userInfo);
}
