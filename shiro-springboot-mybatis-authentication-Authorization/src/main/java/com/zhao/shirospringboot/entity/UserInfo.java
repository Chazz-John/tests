package com.zhao.shirospringboot.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息表(UserInfo)实体类
 *
 * @author makejava
 * @since 2020-11-10 09:53:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 338510133104275610L;
    /**
     * 用户ID（主键、自增）
     */
    private Integer userId;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 状态（0：禁用；1：锁定；2：启用）
     */
    private Integer state;
    /**
     * 身份信息
     * */
    private String roleName;
    /**
     * 权限信息
     * */
    private String permission;
}