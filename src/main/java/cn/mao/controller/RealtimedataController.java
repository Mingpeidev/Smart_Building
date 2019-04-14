package cn.mao.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public List<Realtimedata> getRealtimedata() {
		System.out.println("读取控制信息");

		List<Realtimedata> list = realtimedataService.getRealtimedataAll();

		return list;
	}

	@RequestMapping("/Sensorcontrol")
	@ResponseBody
	public Map<String, Object> Sensorcontrol(String lamp, String air, String alarm, String door) {

		System.out.println("实时控制");
		Map<String, Object> map = new HashMap<String, Object>();

		String data = "0" + CharFormatUtil.binaryString2hexString(lamp + air + alarm + door);

		Rxtx_sensor.sendMsg(data);

		map.put("data", "success");

		return map;

	}

}
