package cn.mao.controller;

import cn.mao.Sensorandrfid.Rxtx_Rfid;
import cn.mao.pojo.Resident;
import cn.mao.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        System.out.println("添加住户门禁信息");
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
     * 分页获取住户信息表
     *
     * @return
     */
    @RequestMapping("/getResidentList")
    @ResponseBody
    public Map<String, Object> getResidentList(int page, int limit) {
        Map<String, Object> map = new HashMap<String, Object>();
        System.out.println("获取住户门禁信息表");

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

        if (Rxtx_Rfid.judgelink() != null) {

            Rxtx_Rfid.sendMsg("0200000446529C03");

            try {
                Thread.sleep(1000);// 延时执行获取卡号操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String ID = Rxtx_Rfid.getID();
            System.out.println("获取卡号：" + ID);
            if (!ID.equals("")) {
                map.put("ID", ID);
            } else {
                map.put("ID", "fail");
            }
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

        System.out.println("修改住户门禁信息");

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

        System.out.println("删除住户门禁信息");

        residentService.deleteResidentInfo(id);

        map.put("data", "删除成功");

        return map;
    }

}
