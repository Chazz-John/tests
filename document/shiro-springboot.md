# shiro-springboot 实现简单的登陆认证

## 搭建springboot环境

1. 导入相关依赖

   ```xml
   		<dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
               <version>2.3.5.RELEASE</version>
           </dependency>
           <dependency>
               <groupId>org.apache.shiro</groupId>
               <artifactId>shiro-spring</artifactId>
               <version>1.6.0</version>
           </dependency>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-thymeleaf</artifactId>
               <version>2.3.5.RELEASE</version>
           </dependency>
   ```
   
2. 代码实现

   1. 通过官网，我们可以知道shiro有三大核心要素：

      1. **subject -> ShiroFilterFactoryBean**

         在config目录中新建ShiroConfig类，并定义`getShiroFilterFactoryBean()`方法，传入一个`DefaultWebSecurityManager`，并返回一个`ShiroFilterFactoryBean`

         ``` java
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
                     return subject;
                 }
         }
         ```

         

      2. **securityManager -> DefaultWebSecurityManager**

         在ShiroConfig类中，定义`getDefaultWebSecurityManager()`方法，传入一个`UserRealm`，并返回一个`DefaultWebSecurityManager`，也就是为前一个方法提供`DefaultWebSecurityManager`

         ```java
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
         }
         ```

         

      3. **realm->手动实现**

         在`config`目录目录中新建一个`UserRealm`类，并继承抽象类`AuthorizingRealm`，并实现相应的方法，这里我们先什么都不做，都放回null;

         ```java
         package com.zhao.shirospringboot.config;
         
         import org.apache.shiro.authc.*;
         import org.apache.shiro.authz.AuthorizationInfo;
         import org.apache.shiro.realm.AuthorizingRealm;
         import org.apache.shiro.subject.PrincipalCollection;
         
         public class UserRealm extends AuthorizingRealm {
             @Override
             /**
              * 授权
              * */
             protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
                 System.out.println("执行了授权操作->doGetAuthorizationInfo");
                 return null;
             }
             /**
              * 认证
              * */
             @Override
             protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
                 System.out.println("执行了认证操作->doGetAuthenticationInfo");
                 return null;
             }
         }
         ```

         再在`ShiroConfig`类中定义一个方法，返回一个`UserRealm`类的实例，这里给出完整步骤的`ShiroConfig`类的代码

         ```java
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
         ```

         

      实际操作中对象创建的顺序 ： realm -> securityManager -> subject

   2. 配置具体代码实现不连接数据库进行登陆认证：

      - 在`config下`的`ShiroConfig`类中的`ShiroFilterFactoryBean`添加代码，对user目录下的页面进行拦截，放行其他页面

        ```java
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
         ```
        ```
        
         - 在controller类中接收前端的用户登陆信息，并将用户信息交给shiro处理
      
           ```java
           @RequestMapping("/toLogin")
               public String toLogin(String username,String pwd,Model model) {
                   //要对subject进行认证，就要首先获取到当前的subject对象
                   //通过之前的例子可以看到，subject需要securityUtils类获取
                   //这个流程在官网的例子中可以看到
                   Subject subject = SecurityUtils.getSubject();
                   UsernamePasswordToken token = new UsernamePasswordToken(username, pwd);
                   try {
                       //这里只是将用户信息交给shiro，并为进行信息认证，信息的认证在UserRealm中进行，如果UserRealm中不做出来，则任何用户信息都会报错。
                       subject.login(token);
                       return "index";
                   }catch (UnknownAccountException e) {
                       model.addAttribute("msg","用户名不存在！");
                       return "login";
                   }catch (IncorrectCredentialsException e){
                       model.addAttribute("msg","用户名或密码错误");
                       return "login";
                   }
               }
           ```
      
      - 在`UserRealm`中的`doGetAuthenticationInfo`方法中进行用户信息认证，这里先写死帐号密码，进行测试。
      
        ```java
        package com.zhao.shirospringboot.config;
        
        import org.apache.shiro.authc.*;
        import org.apache.shiro.authz.AuthorizationInfo;
        import org.apache.shiro.realm.AuthorizingRealm;
        import org.apache.shiro.subject.PrincipalCollection;
        
        public class UserRealm extends AuthorizingRealm {
            @Override
            /**
             * 授权
             * */
            protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
                System.out.println("执行了授权操作->doGetAuthorizationInfo");
                return null;
            }
            /**
             * 认证
             * */
            @Override
            protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
                System.out.println("执行了认证操作->doGetAuthenticationInfo");
                String username = "1234";
                String password = "1234";
                UsernamePasswordToken userToken = (UsernamePasswordToken) token;
                if (!userToken.getUsername().equals(username)){
                    return null;
                }
                return new SimpleAuthenticationInfo("",password,"");
            }
        }
        ```
      
        