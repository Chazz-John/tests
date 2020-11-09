package com.zhao.shirospringboot.config;

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
                anno:无需认证就可以访问
                authc:必须认证才可以访问
                user:必须拥有"记住我"功能才能用
                perm:拥有对某个资源的权限才能放问
                role:拥有某个角色的权限才能访问
            */
            Map<String, String> filterChainDefinitionMap= new LinkedHashMap<>();
            filterChainDefinitionMap.put("/user/*","authc");
            subject.setFilterChainDefinitionMap(filterChainDefinitionMap);
            subject.setLoginUrl("/login");
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
            return new UserRealm();
        }
}
