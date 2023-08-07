package com.kinduberre.chama.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
	
	@GetMapping("/home")
	public String dashboard() {
		return "index";
	}
	
	@GetMapping("/loan-amortization")
	public String loanAmortization() {
		return "loan-amortization";
	}
	
	@GetMapping("/loan-application")
	public String loanApplication() {
		return "loan-application";
	}
}
