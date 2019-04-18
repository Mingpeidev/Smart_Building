package cn.mao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.mao.pojo.Resident;
import cn.mao.pojo.Triprecord;

public interface ResidentMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Resident record);

	int insertSelective(Resident record);

	Resident selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Resident record);

	int updateByPrimaryKey(Resident resident);

	List<Resident> selectResidentAll();

	List<Resident> selectResidentByPage(@Param("page") int page, @Param("limit") int limit);
	
	Resident selectByDoorid(@Param("doorid") String doorid);
}