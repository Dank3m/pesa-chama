package com.kinduberre.chama.controllers;

import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//import com.kinduberre.chama.models.auth.User;

@Controller
public class AuthController {
	@GetMapping("/login")
	public String login() {
		
		return "login";
	}
	
	@GetMapping("/register")
	public String register(//Model model
			) {
//		model.addAttribute("user", new User());
		return "register";
	}
	
	
}
