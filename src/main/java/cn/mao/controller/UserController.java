package cn.mao.controller;

import cn.mao.pojo.User;
import cn.mao.sensor.Rxtx_Rfid;
import cn.mao.sensor.Rxtx_sensor;
import cn.mao.service.UserService;
import cn.mao.util.SsmResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    Map<String, Object> loginMap = new HashMap<String, Object>();
    /**
     * 测试标志位
     */
    private static final boolean TEST_FLAG = false;

    /**
     * 正常访问login页面
     *
     * @return
     */
    @RequestMapping("/login")
    public String login() {

        if (TEST_FLAG) {
            return "login";
        }

        if (Rxtx_sensor.judgelink() == null) {
            System.out.println("开启传感器串口！电脑");
            new Rxtx_sensor().init();
        }

        return "login";
    }

    /**
     * 跳转到主页
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 跳转到智能设置
     *
     * @return
     */
    @RequestMapping("/smartSet")
    public String smartSet() {
        return "smart_set";
    }

    /**
     * 跳转到修改密码
     *
     * @return
     */
    @RequestMapping("/modifyPsd")
    public String modifyPsd() {
        return "modify_psd";
    }

    /**
     * 跳转到门禁
     *
     * @return
     */
    @RequestMapping("/door")
    public String door() {

        if (TEST_FLAG) {
            return "door";
        }

        if (Rxtx_Rfid.judgelink() == null) {
            System.out.println("开启rfid串口");
            Rxtx_Rfid rfid = new Rxtx_Rfid();
            rfid.init();
        }
        return "door";
    }

    /**
     * 跳转到报警信息
     *
     * @return
     */
    @RequestMapping("/alarm")
    public String alarm() {
        return "alarm";
    }

    /**
     * 跳转到控制台
     *
     * @return
     */
    @RequestMapping("/console")
    public String console() {
        return "console";
    }

    /**
     * 登录 加@RequestParam表示重命名，并且此字符串为必须
     *
     * @param username
     * @param password
     * @param session
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/checkLogin")
    @ResponseBody
    public Map<String, Object> checkLogin(@RequestParam("username") String username,
                                          @RequestParam("password") String password,
                                          HttpSession session,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        // 调用service方法
        loginMap.clear();

        try {
            User user = new User();
            if (TEST_FLAG) {
                user.setUsername("mao");
                user.setPassword("123456");
            } else {
                user = userService.checkLogin(username, password);
            }

            if (user != null) {
                loginMap.put("msg", "1");
                session.setAttribute("user", user);

                Cookie ck = new Cookie("username", username);
                String path = request.getContextPath();
                ck.setPath(path);
                ck.setMaxAge(30 * 24 * 60 * 60);
                response.addCookie(ck);

            } else {
                loginMap.put("msg", "用户名或密码错误，请重新登陆！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            loginMap.put("msg", e.getMessage());
        }

        return loginMap;
    }

    /**
     * 在index检查是否登录
     *
     * @param session
     * @return
     */
    @RequestMapping("/judgeLogin")
    @ResponseBody
    public Map<String, Object> judgeLogin(HttpSession session) {
        Map<String, Object> judgeMap = new HashMap<String, Object>();

        User user = (User) session.getAttribute("user");

        if (user != null) {
            judgeMap.put("logininfo", user.getUsername());
        } else {
            judgeMap.put("logininfo", "fail");
        }

        return judgeMap;
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public Map<String, Object> register(User user) {
        Map<String, Object> registerMap = new HashMap<String, Object>();

        if (user.getUsername().length() < 2 || user.getPassword().length() < 6) {
            registerMap.put("data", "格式不对");
        } else if (userService.judge(user) == "yes") {
            userService.register(user);
            registerMap.put("data", "success");
        } else {
            registerMap.put("data", "fail");
        }

        return registerMap;
    }

    /**
     * 注销
     *
     * @param session
     * @return
     */
    @RequestMapping("/outLogin")
    public String outLogin(HttpSession session) {
        // 通过session.invalidate()方法来注销当前的session
        session.invalidate();

        return "login";
    }

    /**
     * 修改密码
     *
     * @param username
     * @param password
     * @param password1
     * @return
     */
    @RequestMapping("/updatePassword")
    @ResponseBody
    public Map<String, Object> updatePassword(@RequestParam("username") String username,
                                              @RequestParam("password") String password,
                                              @RequestParam("password1") String password1) {
        // 调用service方法
        Map<String, Object> updateMap = new HashMap<String, Object>();

        User user = userService.checkLogin(username, password);

        if (user != null) {
            userService.updatePassword(username, password1);
            updateMap.put("msg", "success");
        } else {
            updateMap.put("msg", "fail");
        }

        return updateMap;
    }

    /**
     * 手机端登录
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/loginInPhone")
    @ResponseBody
    public SsmResult loginInPhone(@RequestParam("username") String username,
                                  @RequestParam("password") String password) {
        User user = userService.checkLogin(username, password);
        if (user != null) {
            return SsmResult.ok(1);
        } else {
            return SsmResult.ok(0);
        }
    }

    /**
     * 手机端注册
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/registerInPhone")
    @ResponseBody
    public SsmResult registerInPhone(@RequestParam("username") String username,
                                     @RequestParam("password") String password) {

        User user = new User();

        user.setUsername(username);
        user.setPassword(password);

        if (userService.judge(user) == "yes") {
            userService.register(user);
            return SsmResult.ok(1);
        } else {
            return SsmResult.ok(0);
        }
    }

}
