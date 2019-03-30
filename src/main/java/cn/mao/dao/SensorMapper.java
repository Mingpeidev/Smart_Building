package cn.mao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.mao.pojo.Sensor;

public interface SensorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Sensor record);

    int insertSelective(Sensor record);

    Sensor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sensor record);

    int updateByPrimaryKey(Sensor record);
    
    List<Sensor> selectSensorAll();
    
    List<Sensor> selectSensorByPage(@Param("page")int page,@Param("limit")int limit);
}