package com.zhao.shirospringboot.config;

import com.zhao.shirospringboot.entity.UserInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 描述:密码加密工具
 *
 * @author Chazz
 * @create 2020-11-09 21:54
 */
public class PwdConfig {
    public String getPwd(String username,String pwd,String salt){
        //加密方式
        String hashAlgorithmName = "MD5";
        //加密次数
        int hashInteractions = 2;
        //将得到的result放到数据库中就行了。
        String  result = new SimpleHash(hashAlgorithmName, pwd, salt, hashInteractions).toHex();
        return result;
    }
}
