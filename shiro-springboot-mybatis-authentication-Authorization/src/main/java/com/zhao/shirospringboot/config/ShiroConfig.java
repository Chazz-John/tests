package com.zhao.shirospringboot.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chazz
 */
@Configuration
public class ShiroConfig {

        /**
         * subject -> ShiroFilterFactoryBean
         * */
        @Bean(name = "shiroFilterFactoryBean")
        public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("manager") DefaultWebSecurityManager manager){
            ShiroFilterFactoryBean subject = new ShiroFilterFactoryBean();
            //设置安全管理器
            subject.setSecurityManager(manager);
            //添加shiro的内部过滤器
            /*
                权限标识:相对于请求而言
                anon:无需认证就可以访问
                authc:必须认证才可以访问
                user:必须拥有"记住我"功能才能用
                perm:拥有对某个资源的权限才能放问
                role:拥有某个角色的权限才能访问
            */
            Map<String, String> filterChainDefinitionMap= new LinkedHashMap<>();
            //配置不需要权限就能访问的静态资源
            filterChainDefinitionMap.put("/static/**", "anon");
            filterChainDefinitionMap.put("/css/**","anon");
            filterChainDefinitionMap.put("/js/**","anon");
            filterChainDefinitionMap.put("/image/**","anon");
            //设置user目录下的页面序言登录之后才能访问
            filterChainDefinitionMap.put("/user/*","authc");
            //设置登录请求
            subject.setLoginUrl("/login");
            // 设置登录成功之后跳转的页面
            subject.setSuccessUrl("/index");
            //将过滤器注入到过滤链中
            subject.setFilterChainDefinitionMap(filterChainDefinitionMap);
            return subject;
        }

        /**
         * securityManager -> DefaultWebSecurityManager
         * */
        @Bean(name = "manager")
        public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm realm){
            DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
            //关联UserRealm
            securityManager.setRealm(realm);
            return securityManager;
        }

        /**
         * realm->手动实现,自己自定义类:第一步
         * */
        @Bean
        public UserRealm userRealm(){
            UserRealm userRealm = new UserRealm();
            //设置加密方式
            userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
            System.out.println("加密注入成功");
            return userRealm;
        }

        /**
         * 配置密码加密方式
         * */
        @Bean
        public HashedCredentialsMatcher hashedCredentialsMatcher(){
            HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
            //设置加密次数
            matcher.setHashAlgorithmName("MD5");//散列算法:这里使用MD5算法;
            System.out.println("设置加密方式->md5");
            //设置加密迭代的次数
            matcher.setHashIterations(2);
            //设置加密的编码: true为hex编码,false为base64编码
            matcher.setStoredCredentialsHexEncoded(true);
            return matcher;
        }

        @Bean
        public ShiroDialect getShiroDialect(){
            return new ShiroDialect();
        }
}
