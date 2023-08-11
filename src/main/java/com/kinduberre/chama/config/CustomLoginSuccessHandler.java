package com.kinduberre.chama.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

//import com.kinduberre.jumiaapi.repository.AuditRepo;

@Configuration
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler{
	
//	@Autowired
//	private	AuditRepo auditRepo;
	
	public CustomLoginSuccessHandler() {
        super();
    }
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {
		handle(request, response, authentication);
        clearAuthenticationAttributes(request);
		
	}
	
	// IMPL

    protected void handle(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
        final String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		System.out.println("Target URL: " + targetUrl);
		redirectStrategy.sendRedirect(request, response, targetUrl);
    }
	
	protected String determineTargetUrl(Authentication authentication) {
		
		String url = "/login?error=true";

		// Fetch the roles from Authentication object
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		List<String> roles = new ArrayList<String>();
		for (GrantedAuthority a : authorities) {
			roles.add(a.getAuthority());
		}

		System.out.println("Roles: " + roles);
		// check user role and decide the redirect URL
		if (roles.contains("ADMIN_USER")) {
//			auditRepo.loginAudit(authentication.getName());
			url = "/home";
		} 
		else if (roles.contains("SITE_USER")) {
//			auditRepo.loginAudit(authentication.getName());
			url = "/home";
		}
		
		else if (roles.contains("SUPER_USER")) {
//			auditRepo.loginAudit(authentication.getName());
			url = "/home";
		}

		return url;
	}
	
	/**
     * Removes temporary authentication-related data which may have been stored in the session
     * during the authentication process.
     */
    protected final void clearAuthenticationAttributes(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }


}
