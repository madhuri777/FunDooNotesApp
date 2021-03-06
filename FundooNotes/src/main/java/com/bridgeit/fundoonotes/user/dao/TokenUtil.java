package com.bridgeit.fundoonotes.user.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TokenUtil {

	@Autowired
	  private RedisTemplate<String, String> redisTemplate;
	  
	  public void setToken(String key, String token) {
		  System.out.println("stored tocken in redis successfully "+key+token);
		  redisTemplate.opsForValue().set(key, token);
	  }	  
	  public String getToken(String key) {
		  System.out.println("get"+key);
		  return redisTemplate.opsForValue().get(key);
	  }
	  public void deleteUser(String key) {
		  System.out.println("deleted key successfully "+key);
		  redisTemplate.delete(key);
	  }	  
}
