# shrio

## 一、概述

### 1.简介	

**Apache Shiro™**是一个强大且易用的Java安全框架，可以完成身份验证、授权、密码和会话管理Shiro 不仅可以用在 JavaSE 环境中，也可以用在 JavaEE 环境中，同样也可以整和在各种集成框架中	

### 2.功能	

主要功能：

​			![img](https://atts.w3cschool.cn/attachments/image/wk/shiro/1.png)

​	**Authentication**：身份认证 / 登录，验证用户是不是拥有相应的身份

​	**Authorization**：授权，即权限验证，验证某个已认证的用户是否拥有某个权限；即判断用户是否能做事情，常见的如：验证某个用户是否拥有某个角色。或者细粒度的验证某个用户对某个资源是否具有某个权限；

​	**Session** **Management**：会话管理，即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中；会话可以是普通 JavaSE 环境的，也可以是如 Web 环境的；

​	**Cryptography**：加密，保护数据的安全性，如密码加密存储到数据库，而不是明文存储;

​	**Caching**：缓存，比如用户登录后，其用户信息、拥有的角色 / 权限不必每次去查，这样可以提高效率；

​	**Web Support**：Web 支持，可以非常容易的集成到 Web 环境；

​	**Caching**：缓存，比如用户登录后，其用户信息、拥有的角色 / 权限不必每次去查，这样可以提高效率；

​	**Concurrency**：shiro 支持多线程应用的并发验证，即如在一个线程中开启另一个线程，能把权限自动传播过去；

​	**Testing**：提供测试支持；

​	**Run As**：允许一个用户假装为另一个用户（如果他们允许）的身份进行访问；

​	**Remember Me**：记住我，这个是非常常见的功能，即一次登录后，下次再来的话不用登录了。

​	**记住一点，Shiro 不会去维护用户、维护权限；这些需要我们自己去设计 / 提供；然后通过相应的接口注入给 Shiro 即可。**

### 3.从外部看

​			![img](https://atts.w3cschool.cn/attachments/image/wk/shiro/2.png)

应用代码直接交互的对象是Subject，也就是说Shiro的对外API核心就是Subject；其每个API的含义：

​	**Subject**：主体，代表了当前 “用户”，这个用户不一定是一个具体的人，与当前应用交互的任何东西都是 Subject，如网络爬虫，机器人等；即一个抽象概念；所有 Subject 都绑定到 SecurityManager，与 Subject 的所有交互都会委托给 SecurityManager；可以把 Subject 认为是一个门面；SecurityManager 才是实际的执行者；

​	**SecurityManager**：安全管理器；即所有与安全有关的操作都会与 SecurityManager 交互；且它管理着所有 Subject；可以看出它是 Shiro 的核心，它负责与后边介绍的其他组件进行交互，如果学习过 SpringMVC，你可以把它看成 DispatcherServlet 前端控制器；

​	**Realm**：域，Shiro 从从 Realm 获取安全数据（如用户、角色、权限），就是说 SecurityManager 要验证用户身份，那么它需要从 Realm 获取相应的用户进行比较以确定用户身份是否合法；也需要从 Realm 得到用户相应的角色 / 权限进行验证用户是否能进行操作；可以把 Realm 看成 DataSource，即安全数据源。

也就是说对于我们而言，最简单的一个 Shiro 应用：

1. 应用代码通过 Subject 来进行认证和授权，而 Subject 又委托给 SecurityManager；
2. 我们需要给 Shiro 的 SecurityManager 注入 Realm，从而让 SecurityManager 能得到合法的用户及其权限进行判断。

**从以上也可以看出，Shiro 不提供维护用户 / 权限，而是通过 Realm 让开发人员自己注入。**

### 4. 外部架构

 			![img](https://atts.w3cschool.cn/attachments/image/wk/shiro/3.png)

​	**Subject**：主体，可以看到主体可以是任何可以与应用交互的 “用户”；

​	**SecurityManager**：相当于 SpringMVC 中的 DispatcherServlet 或者 Struts2 中的 FilterDispatcher；是 Shiro 的心脏；所有具体的交互都通过 SecurityManager 进行控制；它管理着所有 Subject、且负责进行认证和授权、及会话、缓存的管理。

​	**Authenticator**：认证器，负责主体认证的，这是一个扩展点，如果用户觉得 Shiro 默认的不好，可以自定义实现；其需要认证策略（Authentication Strategy），即什么情况下算用户认证通过了；

​	**Authrizer**：授权器，或者访问控制器，用来决定主体是否有权限进行相应的操作；即控制着用户能访问应用中的哪些功能；

​	**Realm**：可以有 1 个或多个 Realm，可以认为是安全实体数据源，即用于获取安全实体的；可以是 JDBC 实现，也可以是 LDAP 实现，或者内存实现等等；由用户提供；注意：Shiro 不知道你的用户 / 权限存储在哪及以何种格式存储；所以我们一般在应用中都需要实现自己的 Realm；

​	**SessionManager**：如果写过 Servlet 就应该知道 Session 的概念，Session 呢需要有人去管理它的生命周期，这个组件就是 SessionManager；而 Shiro 并不仅仅可以用在 Web 环境，也可以用在如普通的 JavaSE 环境、EJB 等环境；所以呢，Shiro 就抽象了一个自己的 Session 来管理主体与应用之间交互的数据；这样的话，比如我们在 Web 环境用，刚开始是一台 Web 服务器；接着又上了台 EJB 服务器；这时想把两台服务器的会话数据放到一个地方，这个时候就可以实现自己的分布式会话（如把数据放到 Memcached 服务器）；

​	**SessionDAO**：DAO 大家都用过，数据访问对象，用于会话的 CRUD，比如我们想把 Session 保存到数据库，那么可以实现自己的 SessionDAO，通过如 JDBC 写到数据库；比如想把 Session 放到 Memcached 中，可以实现自己的 Memcached SessionDAO；另外 SessionDAO 中可以使用 Cache 进行缓存，以提高性能；

​	**CacheManager**：缓存控制器，来管理如用户、角色、权限等的缓存的；因为这些数据基本上很少去改变，放到缓存中后可以提高访问的性能

​	**Cryptography**：密码模块，Shiro 提高了一些常见的加密组件用于如密码加密 / 解密的。

到此 Shiro 架构及其组件就认识完了，接下来挨着学习 Shiro 的组件吧。

### 5. 认证流程

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200923201850784.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQ0OTgzOA==,size_16,color_FFFFFF,t_70#pic_center)

## 2快速入门

### 1. 拷贝案例

1. 按照官网提示找到 快速入门案例

   ![image-20201108162819486](/home/chazz/.config/Typora/typora-user-images/image-20201108162819486.png)

   ​	GitHub地址：[shiro/samples/quickstart/](https://github.com/apache/shiro/tree/master/samples/quickstart)

   ​	从GitHub 的文件中可以看出这个快速入门案例是一个 Maven 项目

2. 新建一个 Maven 工程，删除其 src 目录，将其作为父工程

3. 在父工程中新建一个 Maven 模块

   ![image-20201108164858176](/home/chazz/.config/Typora/typora-user-images/image-20201108164858176.png)

4. 复制快速入门案例 POM.xml 文件中的依赖 （版本号自选）

   ``` xml
   <dependencies>
           <dependency>
               <groupId>org.apache.shiro</groupId>
               <artifactId>shiro-core</artifactId>
               <version>1.4.1</version>
           </dependency>
           <dependency>
               <groupId>org.slf4j</groupId>
               <artifactId>jcl-over-slf4j</artifactId>
               <version>1.7.29</version>
           </dependency>
           <dependency>
               <groupId>org.slf4j</groupId>
               <artifactId>slf4j-log4j12</artifactId>
               <version>1.7.29</version>
           </dependency>
           <dependency>
               <groupId>log4j</groupId>
               <artifactId>log4j</artifactId>
               <version>1.2.17</version>
           </dependency>
   </dependencies>
   ```

5. 把快速入门案例中的 resource 下的`log4j.properties` 复制下来

   ![image-20201108165358666](/home/chazz/.config/Typora/typora-user-images/image-20201108165358666.png)

6. 复制一下 `shiro.ini` 文件：同上

7. 复制一下 `Quickstart.java` 文件：同上
   如果有导包的错误，把那两个错误的包删掉，就会自动导对的包了，快速入门案例中用的方法过时了

   报错：![image-20201108165609734](/home/chazz/.config/Typora/typora-user-images/image-20201108165609734.png)

   解决办法:

   ![image-20201108165538498](/home/chazz/.config/Typora/typora-user-images/image-20201108165538498.png)

8. 运行 `Quickstart.java`，得到结果

   ![image-20201108164810961](/home/chazz/.config/Typora/typora-user-images/image-20201108164810961.png)

### 2.分析案例

1. 通过 SecurityUtils 获取当前执行的用户 Subject

   ``` java
   // 通过securityManager获取当前执行的用户：
   Subject currentUser = SecurityUtils.getSubject();
   ```

2. 通过 当前用户拿到 Session

   ```java
   // 使用Session做一些事情（不需要Web或EJB容器！！！）
   Session session = currentUser.getSession();
   ```

3. 用 Session 存值取值

   ```java
   session.setAttribute("someKey", "aValue");
   String value = (String) session.getAttribute("someKey");
   ```

4. 判断用户是否被认证

   ```java
   if (value.equals("aValue")) {
               log.info("Retrieved the correct value! [" + value + "]");
           }
   ```

   

5. 执行登录操作

   ```java
   if (!currentUser.isAuthenticated()) {
               UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
               token.setRememberMe(true);
               try {
                   currentUser.login(token);
   ```

6. 打印其标识主体

   ```java
   log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
   ```

7. 注销

   ```java
   currentUser.logout();
   ```

8. 总览

   ```java
   //The easiest way to create a Shiro SecurityManager with configured
           //realms, users, roles and permissions is to use the simple INI config.
           //We'll do that by using a factory that can ingest a .ini file and
           //return a SecurityManager instance:
   
   
           //使用工厂模式,通过.ini配置文件生成一个工厂实例
           Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
           SecurityManager securityManager = factory.getInstance();
   
           // for this simple example quickstart, make the SecurityManager
           // accessible as a JVM singleton.  Most applications wouldn't do this
           // and instead rely on their container configuration or web.xml for
           // webapps.  That is outside the scope of this simple quickstart, so
           // we'll just do the bare minimum so you can continue to get a feel
           // for things.
           //大多数时候我们是通过容器配置或者xml配置来拿到subject对象
           SecurityUtils.setSecurityManager(securityManager);
   
           // 现在已经建立了一个简单的Shiro环境，让我们看看您可以做什么：
   
           // 通过securityManager获取当前执行的用户：
           Subject currentUser = SecurityUtils.getSubject();
   
           // 使用Session做一些事情（不需要Web或EJB容器！！！）
           Session session = currentUser.getSession();
           session.setAttribute("someKey", "aValue");
           String value = (String) session.getAttribute("someKey");
           if (value.equals("aValue")) {
               log.info("Retrieved the correct value! [" + value + "]");
           }
   
           // 让我们登录当前用户，以便我们可以检查角色和权限：
           if (!currentUser.isAuthenticated()) {
               //进行登陆
               UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
               token.setRememberMe(true);
               try {
                   currentUser.login(token);
               } catch (UnknownAccountException uae) {
                   log.info("There is no user with username of " + token.getPrincipal());
               } catch (IncorrectCredentialsException ice) {
                   log.info("Password for account " + token.getPrincipal() + " was incorrect!");
               } catch (LockedAccountException lae) {
                   log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                           "Please contact your administrator to unlock it.");
               }
               // ... catch more exceptions here (maybe custom ones specific to your application?
               catch (AuthenticationException ae) {
                   //unexpected condition?  error?
               }
           }
   
           //say who they are:
           //print their identifying principal (in this case, a username):
   		//获取当前登陆的对象,默认只会给出用户名，无法获取密码
           log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
           //test a role:
           if (currentUser.hasRole("schwartz")) {
               log.info("May the Schwartz be with you!");
           } else {
               log.info("Hello, mere mortal.");
           }
   
           //test a typed permission (not instance-level)
           if (currentUser.isPermitted("lightsaber:wield")) {
               log.info("You may use a lightsaber ring.  Use it wisely.");
           } else {
               log.info("Sorry, lightsaber rings are for schwartz masters only.");
           }
   
           //a (very powerful) Instance Level permission:
           if (currentUser.isPermitted("winnebago:drive:eagle5")) {
               log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                       "Here are the keys - have fun!");
           } else {
               log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
           }
   
           //all done - log out!
           currentUser.logout();
   
           System.exit(0);
   ```

