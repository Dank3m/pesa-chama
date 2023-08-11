package com.kinduberre.chama.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

//import com.kinduberre.jumiaapi.repository.AuditRepo;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

//	@Autowired
//	private	AuditRepo auditRepo;
	
    @Override
    public void onLogoutSuccess(
      HttpServletRequest request, 
      HttpServletResponse response, 
      Authentication authentication) 
      throws IOException, ServletException {
//    	auditRepo.logoutAudit(authentication.getName());
        super.setTargetUrlParameter("/login");
        super.onLogoutSuccess(request, response, authentication);
    }
}
