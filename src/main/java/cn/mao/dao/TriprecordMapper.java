package cn.mao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.mao.pojo.Triprecord;

public interface TriprecordMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Triprecord record);

	int insertSelective(Triprecord record);

	Triprecord selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Triprecord record);

	int updateByPrimaryKey(Triprecord record);

	List<Triprecord> selectTriprecordAll();

	List<Triprecord> selectTriprecordByPage(@Param("page") int page, @Param("limit") int limit);
}