package cn.mao.controller;

import cn.mao.pojo.Setting;
import cn.mao.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class SettingController {

    @Autowired
    private SettingService settingService;

    /**
     * 更新设置表
     *
     * @param temp
     * @param humi
     * @param light
     * @param timeon
     * @param timeoff
     * @param smart
     * @return
     */
    @RequestMapping("/updateSetting")
    @ResponseBody
    public Map<String, Object> updateSetting(Integer temp, Integer humi, Integer light, String timeon, String timeoff,
                                             String smart) {
        Map<String, Object> map = new HashMap<String, Object>();
        System.out.println("更新设置表");

        java.util.Date dateon = null;
        java.util.Date dateoff = null;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", java.util.Locale.US);// 设置日期格式
        try {
            dateon = sdf.parse(timeon);// 转化为java.util.Date
            dateoff = sdf.parse(timeoff);// date指定格式转化为Time
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Time valueon = new Time(dateon.getTime());// 转化为sql.Time
        Time valueoff = new Time(dateoff.getTime());

        System.out.println(valueon + "@" + valueoff + "@");

        Setting setting = new Setting();

        setting.setId(1);
        setting.setTemp(temp);
        setting.setHumi(humi);
        setting.setLight(light);
        setting.setTimeon(valueon);
        setting.setTimeoff(valueoff);
        setting.setSmart(smart);

        settingService.updateSetting(setting);

        map.put("data", "success");

        return map;
    }

    /**
     * 查询设置表，并在设置页上显示
     *
     * @return
     */
    @RequestMapping("/selectSetting")
    @ResponseBody
    public Map<String, Object> selectSetting() {
        Map<String, Object> map = new HashMap<String, Object>();
        System.out.println("查询设置表");

        Setting setting = settingService.selectSetting(1);

        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");// 指定类型

        String timeon = sdfTime.format(setting.getTimeon());// sqltime转为date指定类型
        String timeoff = sdfTime.format(setting.getTimeoff());// sqltime转为date指定类型

        System.out.println(timeoff + "@" + timeon);

        map.put("timeon", timeon);
        map.put("timeoff", timeoff);
        map.put("data", setting);

        return map;
    }

}
