package com.zhao.shirospringboot.service.impl;

import com.zhao.shirospringboot.dao.UserInfoDao;
import com.zhao.shirospringboot.entity.UserInfo;
import com.zhao.shirospringboot.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述:
 *
 * @author Chazz
 * @create 2020-11-10 9:44
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public UserInfo findUserInfoByName(String name) {
        return userInfoDao.findUserInfoByName(name);
    }

    @Override
    public int addUserInfo(UserInfo userInfo) {
        return userInfoDao.addUserInfo(userInfo);
    }
}
