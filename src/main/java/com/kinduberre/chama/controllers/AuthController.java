package com.kinduberre.chama.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kinduberre.chama.exception.RecordNotFoundException;
import com.kinduberre.chama.models.auth.User;
import com.kinduberre.chama.models.messaging.EmailDetails;
import com.kinduberre.chama.services.EmailServiceImpl;
import com.kinduberre.chama.services.UserService;
import com.kinduberre.chama.util.PasswordResetUtil;

import net.bytebuddy.utility.RandomString;

@Controller
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailServiceImpl emailService;
	
	@GetMapping("/login")
	public String login() {
		
		return "login";
	}
	
	@GetMapping("/register")
	public String register(Model model
			) {
		model.addAttribute("user", new User());
		return "register";
	}
	
	@GetMapping(path = {"/recover-password"})
	public String recoverPassword(Model model) 
	{	
		System.out.println("getRecoverPasswordPage");
		
		return "kindu-recover-password";
	}
	
	@PostMapping(path = "/recover-password")
	public String processRecoverPassword(HttpServletRequest request, Model model) 
	{	
		System.out.println("processRecoverPassword");
		String email = request.getParameter("email");
		String token = RandomString.make(30);
		try {
			userService.updateResetPasswordToken(token, email);
			String resetPasswordLink = PasswordResetUtil.getSiteURL(request) + "/reset-password?token=" + token;
			String subject = "Here's the link to reset your password";
			String content = "<p>Hello,</p>"
		            + "<p>You have requested to reset your password.</p>"
		            + "<p>Click the link below to change your password:</p>"
		            + "<p><a href=\"" + resetPasswordLink + "\">Change my password</a></p>"
		            + "<br>"
		            + "<p>Ignore this email if you do remember your password, "
		            + "or you have not made the request.</p>";
			
			EmailDetails emailDetails = new EmailDetails(email, content, subject, null);
			System.out.println("Sending Email ...");
			emailService.sendSimpleMail(emailDetails);
			System.out.println("Email sent...");
			model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
			
		} catch (RecordNotFoundException e) {
			model.addAttribute("error", e.getMessage());
			e.printStackTrace();
		}
		return "kindu-recover-password";
	}
	
	@GetMapping(path= "/reset-password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
	    User user = userService.getByResetPasswordToken(token);
	    model.addAttribute("token", token);
	     
	    if (user == null) {
	        model.addAttribute("message", "Invalid Token");
	        return "reset-password";
	    }
	     
	    return "kindu-reset-password";
	}
	
	@PostMapping(path="/reset-password")
	public String processResetPassword(HttpServletRequest request, Model model) {
	    String token = request.getParameter("token");
	    String password = request.getParameter("password");
	    String confirmPassword = request.getParameter("confirmPassword");
	     
	    User user = userService.getByResetPasswordToken(token);
	     
	    if (user == null) {
	        model.addAttribute("message", "Invalid Token");
	        return "kindu-reset-password";
	    } 
	    else if (!password.contentEquals(confirmPassword)) {
	    	model.addAttribute("message", "Please make sure that both password fields match");
	        return "redirect:/kindu-reset-password?token=" + token+"&error=true";
	    }
	    else {           
	        userService.updatePassword(user, password);
	        model.addAttribute("message", "You have successfully changed your password.");
	    }
	     
	    return "kindu-reset-password";
	}
	
	@PostMapping(value="/register")
	public ModelAndView registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, ModelMap modelMap) {
		ModelAndView modelAndView = new ModelAndView("register");
		// Check for the validations
		if(bindingResult.hasErrors()) {
			modelAndView.addObject("successMessage", "Please correct the errors in form!");
			modelAndView.addObject("condition", false);
			modelMap.addAttribute("bindingResult", bindingResult);
		}
		else if(userService.isUserAlreadyPresent(user)){
//			modelAndView.addObject("successMessage", "user already exists!");
			modelAndView.addObject("condition", false);
			ObjectError error = new ObjectError("userId","An account already exists for this email.");
			bindingResult.addError(error);
			modelMap.addAttribute("bindingResult", bindingResult);
		}
		
		else if (!user.getPassword().equals(user.getConfirmPassword())) {
//			modelAndView.addObject("successMessage", "Please make sure that both password fields match.");
			modelAndView.addObject("condition", false);
			ObjectError error = new ObjectError("password","Please make sure that both password fields match");
			bindingResult.addError(error);
			modelMap.addAttribute("bindingResult", bindingResult);
		}
//		 we will save the user if, no binding errors
		
		else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User is registered successfully!");
			modelAndView.addObject("condition", true);
			ObjectError error = new ObjectError("password","User is registered successfully!");
			bindingResult.addError(error);
			
			modelMap.addAttribute("bindingResult", bindingResult);
		}
		modelAndView.addObject("user", user);
		return modelAndView;
	}
}
