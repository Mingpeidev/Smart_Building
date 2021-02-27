package cn.mao.controller;

import cn.mao.pojo.TripRecord;
import cn.mao.service.TripRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class TripRecordController {

    @Autowired
    private TripRecordService tripRecordService;

    /**
     * 分页获取出行信息表，并可按住户名查询此住户出行记录
     *
     * @return
     */
    @RequestMapping("/getTriprecordList")
    @ResponseBody
    public Map<String, Object> getTriprecordList(int page, int limit, String residentName) {
        Map<String, Object> map = new HashMap<String, Object>();

        if (residentName != null && residentName != "") {

            System.out.println("查询居民" + residentName + "的出行信息");
            List<TripRecord> list = tripRecordService.getTripRecordAllByName(residentName);

            Integer count = list.size();

            map.put("code", 0);
            map.put("msg", "");
            map.put("data", list);
            map.put("count", count);
        } else {
            System.out.println("显示所有住户出行记录");
            List<TripRecord> list = tripRecordService.getTripRecordByPage((page - 1) * limit, limit);
            List<TripRecord> tripRecords = tripRecordService.getTripRecordAll();

            Integer count = tripRecords.size();

            map.put("code", 0);
            map.put("msg", "");
            map.put("data", list);
            map.put("count", count);
        }

        return map;
    }

}
