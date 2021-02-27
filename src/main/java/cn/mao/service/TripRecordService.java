package cn.mao.service;

import cn.mao.pojo.TripRecord;

import java.util.List;

public interface TripRecordService {
    /**
     * 获取所有居民出行信息
     *
     * @return
     */
    public List<TripRecord> getTripRecordAll();

    /**
     * 分页显示在表格
     *
     * @param page
     * @param limit
     * @return
     */
    public List<TripRecord> getTripRecordByPage(int page, int limit);

    /**
     * 按住户名字查找住户出行记录
     *
     * @param residentName
     * @return
     */
    public List<TripRecord> getTripRecordAllByName(String residentName);

    /**
     * 添加出行记录
     *
     * @param tripRecord
     */
    public void addTripRecord(TripRecord tripRecord);

    /**
     * 查询最新一条出行记录
     *
     * @param doorId
     * @return
     */
    public TripRecord getTripRecordByDoorId(String doorId);

}
