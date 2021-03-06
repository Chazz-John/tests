package com.zhao.shirospringboot.config;

import com.zhao.shirospringboot.dao.UserInfoDao;
import com.zhao.shirospringboot.entity.UserInfo;
import com.zhao.shirospringboot.service.UserInfoService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserInfoDao userInfoDao;

    @Autowired
    UserInfoService userInfoService;

    /**
     * 授权
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("执行了授权操作->doGetAuthorizationInfo");
        //创建一个身份授权类
        SimpleAuthorizationInfo  authorization = new SimpleAuthorizationInfo();
        //从principals中获取当前用户的全部信息
        UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();
        //打印可以看到用户的所有信息
        System.out.println(userInfo);
        //将用户的授权信息交给shiro验证
        authorization.addStringPermission(userInfo.getPermission());
        return authorization;
    }
    /**
     * 认证
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了认证操作->doGetAuthenticationInfo");

        //获取在controller层设置的UsernamePasswordToken,从而获取登录的用户信息
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        System.out.println(userToken.getPassword());
        //打印用户信息
        System.out.println("身份："+userToken);
        System.out.println("凭证："+userToken.getCredentials());
        //判断token是否为空
        if (userToken.getPrincipal()==null){
            return null;
        }
        //查询数据库对应的用户信息
        UserInfo user = userInfoService.findUserInfoByName(userToken.getUsername());
        System.out.printf("user=>"+user.toString());
        if (user == null)
        {
            //未知账号
            throw new UnknownAccountException();
        }

        //判断账号是否被锁定，状态（0：禁用；1：锁定；2：启用）
        if(user.getState() == 0)
        {
            //帐号禁用
            throw new DisabledAccountException();
        }

        if (user.getState() == 1)
        {
            //帐号锁定
            throw new LockedAccountException();
        }
        //参数说明:
        //  principal--当前的主体,可以理解为当前token中的对象
        //  hashedCredentials--数据库中对应用户的加密之后的密码
        //  credentialsSalt--org.apache.shiro.util.ByteSource 类型的salt,数据库中的salt可用ByteSource.Util.bytes()方法转化为对应类型的数据
        //  realmName -- 当前的realm对象
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
            user,
            user.getPassword(),
            ByteSource.Util.bytes(user.getSalt()),
            this.getName());
        System.out.println("info=>"+info);
        return info;
    }
}
