package cn.mao.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.mao.pojo.Triprecord;
import cn.mao.service.TriprecordService;

@Controller
@RequestMapping("/user")
public class TriprecordController {

	@Autowired
	private TriprecordService triprecordService;

	/**
	 * 分页获取出行信息表，并可按住户名查询此住户出行记录
	 * 
	 * @return
	 */
	@RequestMapping("/getTriprecordList")
	@ResponseBody
	public Map<String, Object> getTriprecordList(int page, int limit, String residentname) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (residentname != null && residentname != "") {

			System.out.println("查询居民" + residentname + "的出行信息");
			List<Triprecord> list = triprecordService.getTriprecordAllByname(residentname);

			Integer count = list.size();

			map.put("code", 0);
			map.put("msg", "");
			map.put("data", list);
			map.put("count", count);
		} else {
			System.out.println("显示所有住户出行记录");
			List<Triprecord> list = triprecordService.getTriprecordByPage((page - 1) * limit, limit);
			List<Triprecord> triprecords = triprecordService.getTriprecordAll();

			Integer count = triprecords.size();

			map.put("code", 0);
			map.put("msg", "");
			map.put("data", list);
			map.put("count", count);
		}

		return map;
	}

}
