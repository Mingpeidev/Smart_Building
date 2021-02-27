package cn.mao.service;

import cn.mao.pojo.Sensor;

import java.util.List;

public interface SensorService {
    /**
     * 获取所有传感器信息
     *
     * @return
     */
    public List<Sensor> getSensorAll();

    /**
     * 分页显示在表格
     *
     * @param page
     * @param limit
     * @return
     */
    public List<Sensor> getSensorByPage(int page, int limit);

    /**
     * 插入传感器信息
     *
     * @param sensor
     */
    void insertSensor(Sensor sensor);
}
