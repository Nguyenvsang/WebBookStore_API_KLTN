package com.nhom14.webbookstore.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {

	@GetMapping("/admin")
	public String showAdminHome() {
		return "admin/indexadmin";
	}
	
	@GetMapping("/aboutusadmin")
	public String showAboutUs() {
		return "admin/aboutusadmin";
	}
}
