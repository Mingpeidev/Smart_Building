package cn.mao.service;

import cn.mao.pojo.User;

//Service层接口
public interface UserService {

	// 检验用户登录
	User checkLogin(String username, String password);

	void register(User user);

	String judge(User user);

	void updatePassword(String username, String password);

}
