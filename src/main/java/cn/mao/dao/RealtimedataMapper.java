package cn.mao.dao;

import java.util.List;

import cn.mao.pojo.Realtimedata;

public interface RealtimedataMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Realtimedata record);

	int insertSelective(Realtimedata record);

	Realtimedata selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Realtimedata record);

	int updateByPrimaryKey(Realtimedata record);

	/**
	 * 获取Realtimedata表内容
	 * 
	 * @return
	 */
	List<Realtimedata> selectRealtimedataAll();
}