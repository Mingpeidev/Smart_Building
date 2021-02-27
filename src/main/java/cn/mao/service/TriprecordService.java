package cn.mao.service;

import cn.mao.pojo.Triprecord;

import java.util.List;

public interface TriprecordService {
    /**
     * 获取所有居民出行信息
     *
     * @return
     */
    public List<Triprecord> getTriprecordAll();

    /**
     * 分页显示在表格
     *
     * @param page
     * @param limit
     * @return
     */
    public List<Triprecord> getTriprecordByPage(int page, int limit);

    /**
     * 按住户名字查找住户出行记录
     *
     * @param residentname
     * @return
     */
    public List<Triprecord> getTriprecordAllByname(String residentname);

    /**
     * 添加出行记录
     *
     * @param triprecord
     */
    public void addTriprecord(Triprecord triprecord);

    /**
     * 查询最新一条出行记录
     *
     * @param doorid
     * @return
     */
    public Triprecord getTriprecordByDoorid(String doorid);

}
