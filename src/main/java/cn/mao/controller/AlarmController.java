package cn.mao.controller;

import cn.mao.pojo.Alarm;
import cn.mao.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        int count = alarms.size();

        map.put("code", 0);
        map.put("msg", "");
        map.put("data", alarms);
        map.put("count", count);

        return map;
    }

    @RequestMapping("/editAlarm")
    @ResponseBody
    public Map<String, Object> editAlarm(Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();

        String data = alarmService.updateAlarm(id);

        map.put("data", data);

        return map;
    }

}
