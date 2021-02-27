package cn.mao.dao;

import cn.mao.pojo.Triprecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TriprecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Triprecord triprecord);

    int insertSelective(Triprecord record);

    Triprecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Triprecord record);

    int updateByPrimaryKey(Triprecord record);

    List<Triprecord> selectTriprecordAll();

    List<Triprecord> selectTriprecordAllByname(@Param("residentname") String residentname);

    List<Triprecord> selectTriprecordByPage(@Param("page") int page, @Param("limit") int limit);

    Triprecord selectByDoorid(@Param("doorid") String doorid);
}