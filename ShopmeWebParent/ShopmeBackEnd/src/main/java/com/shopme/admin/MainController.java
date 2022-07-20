package com.shopme.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@RestController
public class MainController {
	
	//@Autowired
	//private UserService service;
	
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
//	@GetMapping(value = "/apiuser")
//	public List<User> getUser() {
//		List<User> users = service.listAll();
//		return users;
//	}
	
	@GetMapping("/login")
	public String viewLoginPage() {
		return "login";
	}

}
