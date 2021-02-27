package cn.mao.dao;

import cn.mao.pojo.TripRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TripRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TripRecord triprecord);

    int insertSelective(TripRecord record);

    TripRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TripRecord record);

    int updateByPrimaryKey(TripRecord record);

    List<TripRecord> selectTripRecordAll();

    List<TripRecord> selectTripRecordAllByName(@Param("residentName") String residentName);

    List<TripRecord> selectTripRecordByPage(@Param("page") int page, @Param("limit") int limit);

    TripRecord selectByDoorId(@Param("doorId") String doorId);
}