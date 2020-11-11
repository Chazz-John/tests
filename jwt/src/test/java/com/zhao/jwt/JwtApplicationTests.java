package com.zhao.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Calendar;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

// @SpringBootTest
class JwtApplicationTests {

	@Test
	void contextLoads() {
		/**
		 * 令牌的生成
		 * */
		//用日期类生成过期时间
		Calendar calendar = Calendar.getInstance();
		//设置过期时间
		calendar.add(Calendar.DATE, 7);

		HashMap<String, Object> map = new HashMap<>();
		map.put("alg","HS256");//算法名称
		map.put("typ","JWT");//token类型
		String token = JWT.create()
			.withHeader(map)//header
			.withClaim("userId", 1)//payload
			.withClaim("userName", "张三")
			.withExpiresAt(calendar.getTime())//令牌过期时间
			.sign(Algorithm.HMAC256("!sfjasfioh"));//签名,第三部分的一部分
		System.out.println(token);
	}

	@Test
	void contextLoads2() {
		//令牌的验证
		JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("!sfjasfioh")).build();
		DecodedJWT decodedJWT = jwtVerifier.verify(
			"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6IuW8oOS4iSIsImV4cCI6MTYwNTEwMDg3MCwidXNlcklkIjoxfQ.SZ3nEmcWrPUoauqdufZdCXJYErC8i_N5XlHzcZ60vgM");
		System.out.println(decodedJWT.getClaim("userId").asInt());
	}
}
