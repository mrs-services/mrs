package mrs.app.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	@GetMapping("loginForm")
	String loginForm() {
		return "login/loginForm";
	}

	@GetMapping("kill")
	void kill() {
		System.exit(-1);
	}
}
