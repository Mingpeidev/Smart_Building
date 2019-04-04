package cn.mao.dao;

import org.apache.ibatis.annotations.Param;

import cn.mao.pojo.User;

public interface UserMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	User findByUsername(String username);

	void registerByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

	void updatePassword(@Param("username") String username, @Param("password") String password);
}