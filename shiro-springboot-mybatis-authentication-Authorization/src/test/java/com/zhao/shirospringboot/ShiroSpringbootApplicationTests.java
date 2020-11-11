package com.zhao.shirospringboot;

import com.alibaba.druid.pool.DruidDataSource;
import com.zhao.shirospringboot.dao.UserInfoDao;
import com.zhao.shirospringboot.entity.UserInfo;
import com.zhao.shirospringboot.service.UserInfoService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
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
	@Autowired
	UserInfoService userService;

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

	@Test
	void contextLoads2() throws SQLException {
		System.out.println(userService.findUserInfoByName("chazz"));
	}

	@Autowired
	private UserInfoDao userDao;

	/**
	 * 新增用户
	 * 账号密码的加密、加盐
	 */
	@Test
	public void addUser()
	{
		String originalPassword = "123456"; //原始密码
		String hashAlgorithmName = "MD5"; //加密方式
		int hashIterations = 2; //加密的次数

		//盐
		String salt = new SecureRandomNumberGenerator().nextBytes().toHex();

		//加密
		SimpleHash simpleHash = new SimpleHash(hashAlgorithmName, originalPassword, salt, hashIterations);
		String encryptionPassword = simpleHash.toString();

		//创建用户信息
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName("pan_junbiao的博客_02");
		userInfo.setPassword(encryptionPassword);
		userInfo.setSalt(salt);
		userInfo.setState(2);

		//执行新增
		userDao.addUserInfo(userInfo);

		//打印结果
		System.out.println("用户ID：" + userInfo.getUserId());
		System.out.println("用户姓名：" + userInfo.getUserName());
		System.out.println("原始密码：" + originalPassword);
		System.out.println("加密密码：" + userInfo.getPassword());
		System.out.println("盐：" + userInfo.getSalt());
	}

}
