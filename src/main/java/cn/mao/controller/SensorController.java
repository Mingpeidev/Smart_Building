package cn.mao.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.mao.Sensorandrfid.Rxtx_sensor;
import cn.mao.pojo.Sensor;

import cn.mao.service.SensorService;
import cn.mao.util.CharFormatUtil;

@Controller
@RequestMapping("/user")
public class SensorController {

	@Autowired
	private SensorService sensorService;

	/**
	 * 折线图数据获取
	 * 
	 * @return
	 */
	@RequestMapping("/getSensorList")
	@ResponseBody
	public Map<String, Object> getSensorList() {
		System.out.println("读取温湿度数据并显示在折线图");
		Map<String, Object> map = new HashMap<>();

		List<Sensor> list = sensorService.getSensorAll();

		if (Rxtx_sensor.judgelink() != null) {
			map.put("list", list);
			map.put("data", "success");
		} else {
			map.put("list", list);
			map.put("data", "fail");// 标志位，标志是否动态变化
		}

		return map;
	}

	/**
	 * 表格数据获取 如果需要返回JSON，XML或自定义mediaType内容到页面，则需要在对应的方法上加上@ResponseBody注解
	 * 
	 * @param page
	 * @param limit
	 * @return
	 */
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

	/**
	 * 控制信息获取
	 * 
	 * @return
	 */
	@RequestMapping("/getRealtimedata")
	@ResponseBody
	public Map<String, Object> getRealtimedata() {

		Map<String, Object> map = new HashMap<String, Object>();

		String human = "";
		String control = "";
		String lamp_control = "";
		String air_control = "";
		String alarm_control = "";
		String door_control = "";

		Set<String> keySet = Rxtx_sensor.dataAll.keySet();
		Iterator<String> it1 = keySet.iterator();
		while (it1.hasNext()) {
			String ID = it1.next();
			if (ID.equals("F8 DE 01")) {
				human = Rxtx_sensor.dataAll.get(ID);
			} else if (ID.equals("14 24 01")) {
				control = Rxtx_sensor.dataAll.get(ID);
			}
		}
		if (!control.equals("")) {
			System.out.println("读取控制信息");
			String x = CharFormatUtil.hexString2binaryString(control);

			lamp_control = x.substring(4, 5);
			air_control = x.substring(5, 6);
			alarm_control = x.substring(6, 7);
			door_control = x.substring(7, 8);

			map.put("lamp", lamp_control);
			map.put("air", air_control);
			map.put("alarm", alarm_control);
			map.put("door", door_control);
		}
		return map;
	}

	/**
	 * 网页控制执行器
	 * 
	 * @param lamp
	 * @param air
	 * @param alarm
	 * @param door
	 * @return
	 */
	@RequestMapping("/Sensorcontrol")
	@ResponseBody
	public Map<String, Object> Sensorcontrol(String lamp, String air, String alarm, String door) {

		System.out.println("实时控制");
		Map<String, Object> map = new HashMap<String, Object>();

		if (Rxtx_sensor.judgelink() != null) {

			String data = "0" + CharFormatUtil.binaryString2hexString(lamp + air + alarm + door);
			Rxtx_sensor.sendMsg(data);
			map.put("data", "success");
		} else {
			map.put("data", "fail");
			System.out.println("未连接串口，操作失败！");
		}

		return map;

	}
}
