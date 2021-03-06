package cn.mao.service;

import cn.mao.pojo.Alarm;

import java.util.List;

public interface AlarmService {

    /**
     * 添加报警信息
     *
     * @param alarm
     */
    public void addAlarmInfo(Alarm alarm);

    /**
     * 获取未处理的报警信息
     *
     * @return
     */
    public List<Alarm> getAlarms();

    /**
     * 更新处理信息
     *
     * @param id
     */
    public String updateAlarm(Integer id);

}
