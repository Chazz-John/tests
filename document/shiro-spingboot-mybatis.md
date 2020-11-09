# shiro-spingboot-mybatis

## 第一步新建项目

1. 新建项目时添加的依赖

![image-20201109013829924](/home/chazz/.config/Typora/typora-user-images/image-20201109013829924.png)

2. 在项目中添加`shiro`依赖和`shiro`整合`thymeleaf`的依赖以及`druid`和`logj`的依赖

   ```xml
   <dependency>
       <groupId>org.apache.shiro</groupId>
       <artifactId>shiro-spring-boot-web-starter</artifactId>
       <version>1.7.0</version>
   </dependency>
   
   <dependency>
       <groupId>com.github.theborakompanioni</groupId>
       <artifactId>thymeleaf-extras-shiro</artifactId>
       <version>2.0.0</version>
   </dependency>
   
   <dependency>
       <groupId>com.alibaba</groupId>
       <artifactId>druid</artifactId>
       <version>1.1.21</version>
   </dependency>
   
   <dependency>
       <groupId>log4j</groupId>
       <artifactId>log4j</artifactId>
       <version>1.2.17</version>
   </dependency>
   ```

3. 新建在`resources`文件夹下新建`application.yaml`文件，进行数据库连接配置(个人比较喜欢`yaml`，`properties`文件配置也可以) 一定要将数据库的信息(`username`、`password`、`url`)修改为自己的数据库配置信息

   ```yaml
   spring:
     datasource:
       username: root
       password: 1234
       #?serverTimezone=UTC解决时区的报错
       url: jdbc:mysql://localhost:3306/tests?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8
       driver-class-name: com.mysql.cj.jdbc.Driver
       type: com.alibaba.druid.pool.DruidDataSource
   
       #Spring Boot 默认是不注入这些属性值的，需要自己绑定
       #druid 数据源专有配置
       initialSize: 5
       minIdle: 5
       maxActive: 20
       maxWait: 60000
       timeBetweenEvictionRunsMillis: 60000
       minEvictableIdleTimeMillis: 300000
       validationQuery: SELECT 1 FROM DUAL
       testWhileIdle: true
       testOnBorrow: false
       testOnReturn: false
       poolPreparedStatements: true
   
       #配置监控统计拦截的filters，stat:监控统计、log4j：日志记录、wall：防御sql注入
       #如果允许时报错  java.lang.ClassNotFoundException: org.apache.log4j.Priority
       #则导入 log4j 依赖即可，Maven 地址：https://mvnrepository.com/artifact/log4j/log4j
       filters: stat,wall,log4j
       maxPoolPreparedStatementPerConnectionSize: 20
       useGlobalDataSourceStat: true
       connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
   ```

4. 现在需要自己为 `DruidDataSource` 绑定全局配置文件中的参数，再添加到容器中，而不再使用 Spring Boot 的自动生成了；我们需要 自己添加` DruidDataSource` 组件到容器中，并绑定属性；

   ``` java
   package com.zhao.shirospringboot.config;
   
   import com.alibaba.druid.pool.DruidDataSource;
   import org.springframework.boot.context.properties.ConfigurationProperties;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   
   import javax.sql.DataSource;
   
   /**
    *
    *@author chazz
    * */
   @Configuration
   public class DruidConfig {
       /**
       *将自定义的 Druid数据源添加到容器中，不再让 Spring Boot 自动创建
       *绑定全局配置文件中的 druid 数据源属性到 com.alibaba.druid.pool.DruidDataSource从而让它们生效
       *@ConfigurationProperties(prefix = "spring.datasource")：作用就是将 全局配置文件中
       *前缀为 spring.datasource的属性值注入到 com.alibaba.druid.pool.DruidDataSource 的同名参数中
       */
       @ConfigurationProperties(prefix = "spring.datasource")
       @Bean
       public DataSource druidDataSource() {
           return new DruidDataSource();
       }
   }
   ```

5. 编写测试类测试数据库连接

   ```java
   package com.zhao.shirospringboot;
   
   import com.alibaba.druid.pool.DruidDataSource;
   import org.junit.jupiter.api.Test;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.boot.test.context.SpringBootTest;
   
   import javax.sql.DataSource;
   import java.sql.Connection;
   import java.sql.SQLException;
   
   @SpringBootTest
   class ShiroSpringbootApplicationTests {
   
   	//DI注入数据源
   	@Autowired
   	DataSource dataSource;
   
   	@Test
   	void contextLoads() throws SQLException {
   		//看一下默认数据源
   		System.out.println(dataSource.getClass());
   		//获得连接
   		Connection connection =   dataSource.getConnection();
   		System.out.println(connection);
   
   		DruidDataSource druidDataSource = (DruidDataSource) dataSource;
   		System.out.println("druidDataSource 数据源最大连接数：" + druidDataSource.getMaxActive());
   		System.out.println("druidDataSource 数据源初始化连接数：" + druidDataSource.getInitialSize());
   
   		//关闭连接
   		connection.close();
   	}
   
   }
   
   ```

6. 打印输出如下，则表示应用成功

   ![image-20201109094014887](C:\Users\Chazz\OneDrive\图片\文档图片\image-20201109094014887.png)

## 第二部将前一个模块的代码复制过来，并进行相应的修改，完成与`mybatis`的整合

1. 

