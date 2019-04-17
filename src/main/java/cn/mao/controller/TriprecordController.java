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
	 * 分页获取出行信息表
	 * 
	 * @return
	 */
	@RequestMapping("/getTriprecordList")
	@ResponseBody
	public Map<String, Object> getTriprecordList(int page, int limit) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Triprecord> list = triprecordService.getTriprecordByPage((page - 1) * limit, limit);
		List<Triprecord> triprecords = triprecordService.getTriprecordAll();

		Integer count = triprecords.size();

		map.put("code", 0);
		map.put("msg", "");
		map.put("data", list);
		map.put("count", count);

		return map;
	}

}
