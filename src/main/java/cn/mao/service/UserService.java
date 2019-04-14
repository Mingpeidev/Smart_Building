package cn.mao.service;

import cn.mao.pojo.User;

//Service层接口
public interface UserService {

	/**
	 * 检验用户登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	User checkLogin(String username, String password);

	/**
	 * 注册
	 * 
	 * @param user
	 */
	void register(User user);

	/**
	 * 判断用户是否存在
	 * 
	 * @param user
	 * @return
	 */
	String judge(User user);

	/**
	 * 修改密码
	 * 
	 * @param username
	 * @param password
	 */
	void updatePassword(String username, String password);

}
