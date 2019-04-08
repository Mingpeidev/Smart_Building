package cn.mao.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.mao.pojo.Sensor;

import cn.mao.service.SensorService;

@Controller
@RequestMapping("/user")
public class SensorController {

	@Autowired
	private SensorService sensorService;

	// 折线图数据获取
	@RequestMapping("/getSensorList")
	@ResponseBody
	public List<Sensor> getSensorList() {
		System.out.println("读取温湿度数据并显示在折线图");
		List<Sensor> list = sensorService.getSensorAll();
		return list;
	}

	// 表格数据获取
	@RequestMapping(value = "getSensorMap")
	@ResponseBody
	public Map<String, Object> getSensorMap(int page, int limit) {

		System.out.println("读取传感器数据并显示在表格" + page + limit);

		Map<String, Object> map = new HashMap<>();

		List<Sensor> list = sensorService.getSensorByPage((page - 1) * limit, limit);
		List<Sensor> listall = sensorService.getSensorAll();
		Integer count = listall.size();

		map.put("code", 0);
		map.put("msg", "");
		map.put("data", list);
		map.put("count", count);
		return map;

	}

}
