package cn.mao.controller;

import java.sql.Timestamp;
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
import cn.mao.pojo.Realtimedata;
import cn.mao.service.RealtimedataService;
import cn.mao.util.CharFormatUtil;

@Controller
@RequestMapping("/user")
public class RealtimedataController {

	@Autowired
	private RealtimedataService realtimedataService;

	// 控制信息获取
	@RequestMapping("/getRealtimedata")
	@ResponseBody
	public Map<String, Object> getRealtimedata() {
		System.out.println("读取控制信息");

		Map<String, Object> map = new HashMap<String, Object>();

		Realtimedata realtimedata = new Realtimedata();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		// List<Realtimedata> list = realtimedataService.getRealtimedataAll();
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
			System.out.println("哈哈哈：" + ID + " " + Rxtx_sensor.dataAll.get(ID));
		}
		String x = CharFormatUtil.hexString2binaryString(control);

		lamp_control = x.substring(4, 5);
		air_control = x.substring(5, 6);
		alarm_control = x.substring(6, 7);
		door_control = x.substring(7, 8);

		map.put("lamp", lamp_control);
		map.put("air", air_control);
		map.put("alarm", alarm_control);
		map.put("door", door_control);

		return map;
	}

	@RequestMapping("/Sensorcontrol")
	@ResponseBody
	public Map<String, Object> Sensorcontrol(String lamp, String air, String alarm, String door) {

		System.out.println("实时控制");
		Map<String, Object> map = new HashMap<String, Object>();

		String data = "0" + CharFormatUtil.binaryString2hexString(lamp + air + alarm + door);

		String human = "";
		String control = "";
		String lamp_control = "";
		String air_control = "";
		String alarm_control = "";
		String door_control = "";

		String lamp_control2 = "";
		String air_control2 = "";
		String alarm_control2 = "";
		String door_control2 = "";

		Set<String> keySet = Rxtx_sensor.dataAll.keySet();
		Iterator<String> it1 = keySet.iterator();
		while (it1.hasNext()) {
			String ID = it1.next();
			if (ID.equals("F8 DE 01")) {
				human = Rxtx_sensor.dataAll.get(ID);
			} else if (ID.equals("14 24 01")) {
				control = Rxtx_sensor.dataAll.get(ID);
			}
			System.out.println("哈哈哈：" + ID + " " + Rxtx_sensor.dataAll.get(ID));
		}
		String x = CharFormatUtil.hexString2binaryString(control);

		lamp_control = x.substring(4, 5);
		air_control = x.substring(5, 6);
		alarm_control = x.substring(6, 7);
		door_control = x.substring(7, 8);

		if (lamp != lamp_control) {
			Rxtx_sensor.sendMsg(data);
		}
		Set<String> keySet1 = Rxtx_sensor.dataAll.keySet();
		Iterator<String> it11 = keySet1.iterator();
		while (it11.hasNext()) {
			String ID = it11.next();
			if (ID.equals("F8 DE 01")) {
				human = Rxtx_sensor.dataAll.get(ID);
			} else if (ID.equals("14 24 01")) {
				control = Rxtx_sensor.dataAll.get(ID);
			}
			System.out.println("哈哈哈：" + ID + " " + Rxtx_sensor.dataAll.get(ID));
		}
		String x2 = CharFormatUtil.hexString2binaryString(control);

		lamp_control2 = x.substring(4, 5);
		air_control2 = x.substring(5, 6);
		alarm_control2 = x.substring(6, 7);
		door_control2 = x.substring(7, 8);

		if (lamp_control != lamp_control2) {
			map.put("data", "success");
		}

		return map;

	}

}
