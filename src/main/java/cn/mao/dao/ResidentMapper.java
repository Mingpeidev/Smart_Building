package cn.mao.dao;

import cn.mao.pojo.Resident;

public interface ResidentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Resident record);

    int insertSelective(Resident record);

    Resident selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Resident record);

    int updateByPrimaryKey(Resident record);
}