package cn.mao.dao;

import cn.mao.pojo.Resident;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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