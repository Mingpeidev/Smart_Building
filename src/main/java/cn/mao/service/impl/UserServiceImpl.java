package cn.mao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.mao.dao.UserMapper;
import cn.mao.pojo.User;
import cn.mao.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	/*
	 * 检验用户登录业务
	 * 
	 */
	public User checkLogin(String username, String password) {

		User user = userMapper.findByUsername(username);
		if (user != null && user.getPassword().equals(password)) {

			return user;
		}
		return null;
	}

	@Override
	public void register(User user) {
		// TODO Auto-generated method stub
		User user1 = userMapper.findByUsername(user.getUsername());
		System.out.print(user1);
		if(user1 != null && user1.getPassword()!=null){
			
		}else{
			userMapper.registerByUsernameAndPassword(user.getUsername(), user.getPassword());
		}
	}

	@Override
	public String judge(User user) {
		// TODO Auto-generated method stub
		User user1 = userMapper.findByUsername(user.getUsername());
		System.out.print(user1);
		if(user1 != null && user1.getPassword()!=null){
			return "no";
		}
		return "yes";
	}
}
