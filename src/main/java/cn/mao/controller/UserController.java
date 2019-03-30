package cn.mao.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import cn.mao.pojo.User;
import cn.mao.service.UserService;
import cn.mao.util.Rxtx_sensor;
import cn.mao.util.SsmResult;

@Controller
@RequestMapping("/user")

// 这里用了@SessionAttributes，可以直接把model中的user(也就key)放入其中,这样保证了session中存在user这个对象
@SessionAttributes("user")
public class UserController {

	@Autowired
	private UserService userServivce;

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

	@RequestMapping("/console")
	public String console() {
		return "console";
	}

	// 表单提交过来的路径
	@RequestMapping("/checkLogin")
	public String checkLogin(User user, Model model) {
		// 调用service方法
		user = userServivce.checkLogin(user.getUsername(), user.getPassword());
		// 若有user则添加到model里并且跳转到成功页面
		if (user != null) {
			model.addAttribute("user", user);
			return "index";
		}
		return "fail";
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
