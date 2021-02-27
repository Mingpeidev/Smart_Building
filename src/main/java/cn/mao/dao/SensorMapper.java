package cn.mao.dao;

import cn.mao.pojo.Sensor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SensorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Sensor record);

    int insertSelective(Sensor record);

    Sensor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sensor record);

    int updateByPrimaryKey(Sensor record);

    List<Sensor> selectSensorAll();

    // 加 @Param是给参数命名，让xml知道sql传入的参数名字
    List<Sensor> selectSensorByPage(@Param("page") int page, @Param("limit") int limit);
}