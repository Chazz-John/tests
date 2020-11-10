package com.zhao.shirospringboot.entity;

import java.io.Serializable;

/**
 * 权限信息表(PermissionInfo)实体类
 *
 * @author zhao
 * @since 2020-11-10 09:38:02
 */
public class PermissionInfo implements Serializable {

    private static final long serialVersionUID = 715469548377771998L;
    /**
     * 权限ID（主键、自增）
     */
    private Integer permissionId;
    /**
     * 权限编号
     */
    private String permissionCode;
    /**
     * 权限名称
     */
    private String permissionName;


    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    public String toString() {
        return "PermissionInfo{" +
            "permissionId=" + permissionId +
            ", permissionCode='" + permissionCode + '\'' +
            ", permissionName='" + permissionName + '\'' +
            '}';
    }
}