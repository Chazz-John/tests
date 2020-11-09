package com.zhao.shirospringboot.enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chazz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {
    private Integer id;
    private String userName;
    private String password;
    private Integer rolesId;
}
