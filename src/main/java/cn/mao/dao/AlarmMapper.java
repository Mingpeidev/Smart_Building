package cn.mao.dao;

import cn.mao.pojo.Alarm;

import java.util.List;

public interface AlarmMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Alarm alarm);

    int insertSelective(Alarm record);

    Alarm selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Alarm record);

    int updateByPrimaryKey(Alarm record);

    List<Alarm> selectAlarm();
}