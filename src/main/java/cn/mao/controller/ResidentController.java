package cn.mao.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.mao.Sensorandrfid.Rxtx_Rfid;
import cn.mao.pojo.Resident;
import cn.mao.pojo.Sensor;
import cn.mao.service.ResidentService;

@Controller
@RequestMapping("/user")
public class ResidentController {

	@Autowired
	private ResidentService residentService;

	/**
	 * 添加居民门禁信息
	 * 
	 * @param resident
	 * @return
	 */
	@RequestMapping("/addResident")
	@ResponseBody
	public Map<String, Object> addResident(String residentname, String doorid, String sex) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (residentname.length() < 2 || doorid.length() < 8) {
			map.put("data", "失败，格式出错！");
		} else {
			Resident resident = new Resident();

			resident.setResidentname(residentname);
			resident.setDoorid(doorid);
			resident.setSex(sex);

			residentService.addResident(resident);
			map.put("data", "写入成功");
		}

		return map;
	}

	/**
	 * 获取住户信息表
	 * 
	 * @return
	 */
	@RequestMapping("/getResidentList")
	@ResponseBody
	public Map<String, Object> getResidentList(int page, int limit) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Resident> list = residentService.getResidentByPage((page - 1) * limit, limit);
		List<Resident> residents = residentService.getResidentAll();

		Integer count = residents.size();

		map.put("code", 0);
		map.put("msg", "");
		map.put("data", list);
		map.put("count", count);

		return map;
	}

	/**
	 * 获取ic卡号
	 * 
	 * @return
	 */
	@RequestMapping("/searchcard")
	@ResponseBody
	public Map<String, Object> searchcard() {
		Map<String, Object> map = new HashMap<String, Object>();

		Rxtx_Rfid rfid = new Rxtx_Rfid();
		rfid.init();

		Rxtx_Rfid.sendMsg("0200000446529C03");

		String ID = Rxtx_Rfid.getID();
		System.out.println(ID);
		if (!ID.equals("")) {
			map.put("ID", ID);
		} else {
			map.put("ID", "fail");
		}

		return map;
	}

	/**
	 * 修改住户表
	 * 
	 * @param id
	 * @param residentname
	 * @param doorid
	 * @param sex
	 * @return
	 */
	@RequestMapping("/editResidentInfo")
	@ResponseBody
	public Map<String, Object> editResidentInfo(Integer id, String residentname, String doorid, String sex) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (residentname.length() < 2 || doorid.length() < 8) {
			map.put("data", "修改失败，格式出错！");
		} else {
			Resident resident = new Resident();

			resident.setId(id);
			resident.setResidentname(residentname);
			resident.setDoorid(doorid);
			resident.setSex(sex);

			residentService.editResidentInfo(resident);

			map.put("data", "修改成功");
		}

		return map;
	}

	/**
	 * 按id删除住户信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteResidentInfo")
	@ResponseBody
	public Map<String, Object> deleteResidentInfo(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();

		residentService.deleteResidentInfo(id);

		map.put("data", "删除成功");

		return map;
	}

}
