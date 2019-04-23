package cn.mao.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.mao.pojo.Alarm;
import cn.mao.service.AlarmService;

@Controller
@RequestMapping("/user")
public class AlarmController {

	@Autowired
	private AlarmService alarmService;

	@RequestMapping("/addAlarm")
	@ResponseBody
	public Map<String, Object> addAlarm() {
		Map<String, Object> map = new HashMap<String, Object>();

		return map;
	}

	@RequestMapping("/getAlarm")
	@ResponseBody
	public Map<String, Object> getAlarm() {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Alarm> alarms = alarmService.getAlarms();
		map.put("list", alarms);

		return map;
	}

}
