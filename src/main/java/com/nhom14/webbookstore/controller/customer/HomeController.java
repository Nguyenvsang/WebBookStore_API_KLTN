package com.nhom14.webbookstore.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String showHome() {
		return "redirect:/viewbooks";
	}
	
	@GetMapping("/aboutus")
	public String showAboutUs() {
		return "customer/aboutus";
	}
}
