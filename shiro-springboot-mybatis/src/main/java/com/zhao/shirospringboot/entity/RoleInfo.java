package com.zhao.shirospringboot.entity;

import java.io.Serializable;

/**
 * 角色信息表(RoleInfo)实体类
 *
 * @author zhao
 * @since 2020-11-10 09:38:19
 */
public class RoleInfo implements Serializable {

    private static final long serialVersionUID = 200358896830438586L;
    /**
     * 角色ID（主键、自增）
     */
    private Integer roleId;
    /**
     * 角色编号
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;


    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleInfo{" +
            "roleId=" + roleId +
            ", roleCode='" + roleCode + '\'' +
            ", roleName='" + roleName + '\'' +
            '}';
    }
}