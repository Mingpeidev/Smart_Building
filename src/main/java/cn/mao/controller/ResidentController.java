package cn.mao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.mao.pojo.Resident;
import cn.mao.pojo.User;
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

		Resident resident = new Resident();

		resident.setResidentname(residentname);
		resident.setDoorid(doorid);
		resident.setSex(sex);

		residentService.addResident(resident);
		map.put("data", "success");

		return map;
	}

}
