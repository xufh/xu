/**
 * 
 */
package com.hh.sso.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hh.sso.mapper.UserMapper;
import com.hh.sso.pojo.User;
import com.hhkj.common.service.RedisService;

/**
 * @author XUFH
 *
 */
@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RedisService redisService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private static final Map<Integer,Boolean> TYPE = new HashMap<Integer,Boolean>();
	private static final Integer REDIS_TIME = 60 * 30;//单位 s
	//可选参数1、2、3分别代表username、phone、email
	static{
		TYPE.put(1, true);
		TYPE.put(2, true);
		TYPE.put(3, true);
	}
	/**
	 * @param param
	 * @param type
	 * @return
	 */
	public Boolean check(String param, Integer type) {
		if(!TYPE.containsKey(type)){
			return null;
		}
		
		User record = new User();
		switch (type) {
		case 1:
			record.setUsername(param);
			break;
		case 2:
			record.setPhone(param);
			break;
		case 3:
			record.setEmail(param);
			break;
		}
		
		User user = this.userMapper.selectOne(record);
		
		/**
		 * true  该用户名存在 不能注册
		 * false 该用户名不存在 可注册
		 */
		return null != user;
	}
	/**
	 * @param user
	 * @return
	 */
	public Boolean saveUser(User user) {
		user.setId(null);
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
        // 加密处理，使用MD5
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		return this.userMapper.insert(user)==1;//插入成功返回1 用此判断是否 注册成功
	}
	/**
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public String doLogin(User param) throws Exception {
		User record = new User();
		record.setUsername(param.getUsername());
		User user = this.userMapper.selectOne(record);
		if(null == user){
			return null;//用户名不存在
		}
		
		if(!StringUtils.equals(user.getPassword(), DigestUtils.md5Hex(param.getPassword()))){
			return null;//密码错误
		}
		
		//登录成功 将 用户的token保存到redis中
		String token = DigestUtils.md5Hex(System.currentTimeMillis()+user.getUsername());
		this.redisService.set("TOKEN_"+token, MAPPER.writeValueAsString(user), REDIS_TIME);
		return token;
	}
	/**
	 * @param token
	 * @return
	 */
	public User queryUserByToken(String token) throws Exception {
        String key = "TOKEN_" + token;
        String jsonData = this.redisService.get(key);
        if (null == jsonData) {
            // 登录超时
            return null;
        }
        User user = MAPPER.readValue(jsonData, User.class);
        // 重新设置生存时间!!!!
        this.redisService.expire(key, REDIS_TIME);
        return user;
    }
	
}
