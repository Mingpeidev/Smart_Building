package cn.mao.dao;

import java.util.List;

import cn.mao.pojo.Alarm;

public interface AlarmMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Alarm alarm);

	int insertSelective(Alarm record);

	Alarm selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Alarm record);

	int updateByPrimaryKey(Alarm record);

	List<Alarm> selectAlarm();
}