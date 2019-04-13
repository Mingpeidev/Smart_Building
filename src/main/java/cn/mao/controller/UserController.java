package cn.mao.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.mao.pojo.User;
import cn.mao.service.UserService;
import cn.mao.Sensorandrfid.Rxtx_sensor;
import cn.mao.util.SsmResult;

@Controller
@RequestMapping("/user")

public class UserController {

	@Autowired
	private UserService userServivce;
	Map<String, Object> loginmap = new HashMap<String, Object>();

	/**
	 * 电脑端
	 */

	// 正常访问login页面
	@RequestMapping("/login")
	public String login() {

		if (Rxtx_sensor.judgelink() == null) {
			System.out.println("尝试连接！电脑");
			new Rxtx_sensor().init();
		}

		return "login";
	}

	// 跳转到主页
	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	// 跳转到智能设置
	@RequestMapping("/smartset")
	public String smart() {
		return "smartset";
	}

	// 跳转到修改密码
	@RequestMapping("/modifypsd")
	public String modifypsd() {
		return "modifypsd";
	}

	// 跳转到门禁
	@RequestMapping("/door")
	public String door() {
		return "door";
	}

	// 跳转到报警信息
	@RequestMapping("/alarm")
	public String alarm() {
		return "alarm";
	}

	// 跳转到控制台
	@RequestMapping("/console")
	public String console() {
		return "console";
	}

	// 登录 加@RequestParam表示重命名，并且此字符串为必须
	@RequestMapping("/checkLogin")
	@ResponseBody
	public Map<String, Object> checkLogin(@RequestParam("username") String username,
			@RequestParam("password") String password, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		// 调用service方法
		loginmap.clear();

		try {
			User user = userServivce.checkLogin(username, password);

			if (user != null) {
				loginmap.put("msg", "1");
				session.setAttribute("user", user);

				Cookie ck = new Cookie("username", username);
				String path = request.getContextPath();
				ck.setPath(path);
				ck.setMaxAge(30 * 24 * 60 * 60);
				response.addCookie(ck);

			} else {
				loginmap.put("msg", "用户名或密码错误，请重新登陆！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			loginmap.put("msg", e.getMessage());
		}

		return loginmap;
	}

	// 在index检查是否登录
	@RequestMapping("/judgeLogin")
	@ResponseBody
	public Map<String, Object> judgeLogin(HttpSession session) {
		Map<String, Object> judgemap = new HashMap<String, Object>();

		User user = (User) session.getAttribute("user");

		if (user != null) {
			judgemap.put("logininfo", user.getUsername());
		} else {
			judgemap.put("logininfo", "fail");
		}

		return judgemap;
	}

	// 注册
	@RequestMapping("/doregis")
	@ResponseBody
	public Map<String, Object> doregis(User user) {
		Map<String, Object> registermap = new HashMap<String, Object>();

		if (user.getUsername().length() < 2 || user.getPassword().length() < 6) {
			registermap.put("data", "格式不对");
		} else if (userServivce.judge(user) == "yes") {
			userServivce.register(user);
			registermap.put("data", "success");
		} else {
			registermap.put("data", "fail");
		}

		return registermap;
	}

	// 注销
	@RequestMapping("/outLogin")
	public String outLogin(HttpSession session) {
		// 通过session.invalidata()方法来注销当前的session
		session.invalidate();

		return "login";
	}

	// 修改密码
	@RequestMapping("/updatePassword")
	@ResponseBody
	public Map<String, Object> updatePassword(@RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("password1") String password1) {
		// 调用service方法
		Map<String, Object> updatemap = new HashMap<String, Object>();

		User user = userServivce.checkLogin(username, password);

		if (user != null) {
			userServivce.updatePassword(username, password1);
			updatemap.put("msg", "success");
		} else {
			updatemap.put("msg", "fail");
		}

		return updatemap;
	}

	/**
	 * 手机端
	 */
	// 登录
	@RequestMapping("/logininphone")
	@ResponseBody
	public SsmResult logininphone(@RequestParam("username") String username,
			@RequestParam("password") String password) {
		User user = userServivce.checkLogin(username, password);
		if (user != null) {
			return SsmResult.ok(1);
		} else {
			return SsmResult.ok(0);
		}
	}

	// 注册
	@RequestMapping("/registerinphone")
	@ResponseBody
	public SsmResult registerinphone(@RequestParam("username") String username,
			@RequestParam("password") String password) {

		User user = new User();

		user.setUsername(username);
		user.setPassword(password);

		if (userServivce.judge(user) == "yes") {
			userServivce.register(user);
			return SsmResult.ok(1);
		} else {
			return SsmResult.ok(0);
		}
	}

}
