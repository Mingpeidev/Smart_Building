package cn.mao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.mao.dao.AlarmMapper;
import cn.mao.pojo.Alarm;
import cn.mao.service.AlarmService;

@Service
public class AlarmServiceImpl implements AlarmService {
	@Autowired
	private AlarmMapper alarmMapper;

	@Override
	public void addAlarmInfo(Alarm alarm) {
		alarmMapper.insert(alarm);
	}

	@Override
	public List<Alarm> getAlarms() {
		List<Alarm> alarms = alarmMapper.selectAlarm();
		return alarms;
	}

	@Override
	public String updateAlarm(Integer id) {
		Alarm alarm = alarmMapper.selectByPrimaryKey(id);

		if (alarm.getState().equals("未处理")) {
			alarm.setState("已处理");
			alarmMapper.updateByPrimaryKey(alarm);
			return "处理成功";
		}
		return "处理失败";

	}

}
