package cn.mao.service;

import java.util.List;

import cn.mao.pojo.Alarm;

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

}
