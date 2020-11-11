package com.zhao.shirospringboot.dao;

import com.zhao.shirospringboot.entity.PermissionInfo;
import com.zhao.shirospringboot.entity.RoleInfo;
import com.zhao.shirospringboot.entity.UserInfo;
import java.util.List;

/**
 * 描述:
 *
 * @author Chazz
 * @create 2020-11-10 9:40
 */
public interface UserInfoDao {
    UserInfo findUserInfoByName(String name);
    int addUserInfo(UserInfo userInfo);
    // List<Integer> findRoleInfoByUserInfoId(int userInfoId);
    // RoleInfo findRoleInfoById(int roleId);
    // List<Integer> findInfoPerInfoIdByRoleId(int roleId);
    // List<PermissionInfo> findPermissionInfos(int permissionId);
}
