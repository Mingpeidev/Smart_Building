package cn.mao.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.mao.pojo.User;
import cn.mao.service.UserService;
import cn.mao.util.Rxtx_sensor;
import cn.mao.util.SsmResult;

@Controller
@RequestMapping("/user")

public class UserController {

	@Autowired
	private UserService userServivce;
	Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * 电脑端
	 */

	// 正常访问login页面
	@RequestMapping("/login")
	public String login() {
		if (Rxtx_sensor.haha() == null) {
			System.out.println("尝试连接！电脑");
			new Rxtx_sensor().init();
		}
		return "login";
	}

	@RequestMapping("/regis")
	public String regis() {
		return "regis";
	}

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping("/console")
	public String console() {
		return "console";
	}

	// 表单提交过来的路径
	@RequestMapping("/checkLogin")
	@ResponseBody
	public Map<String, Object> checkLogin(@RequestParam("username") String username,
			@RequestParam("password") String password, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		// 调用service方法
		map.clear();
		try {
			User user = userServivce.checkLogin(username, password);

			if (user != null) {
				map.put("msg", "1");
				session.setAttribute("user", user);

				Cookie ck = new Cookie("username", username);
				String path = request.getContextPath();
				ck.setPath(path);
				ck.setMaxAge(30 * 24 * 60 * 60);
				response.addCookie(ck);

			} else {
				map.put("msg", "用户名或密码错误，请重新登陆！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", e.getMessage());
		}

		return map;
	}

	@RequestMapping("/doregis")
	public String doregis(User user, Model model) {

		if (userServivce.judge(user) == "yes") {
			userServivce.register(user);
			return "login";
		} else {
			return "fail";
		}
	}

	// 测试超链接跳转到另一个页面是否可以取到session值
	@RequestMapping("/anotherpage")
	public String hrefpage() {

		return "anotherpage";
	}

	// 注销方法
	@RequestMapping("/outLogin")
	public String outLogin(HttpSession session) {
		// 通过session.invalidata()方法来注销当前的session
		session.invalidate();
		return "login";
	}

	/**
	 * 手机端
	 */
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
